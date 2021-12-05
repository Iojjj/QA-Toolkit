package com.github.iojjj.bootstrap.qatoolkit.ruler

import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.coroutines.launchUnit
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import com.github.iojjj.bootstrap.qatoolkit.getColor
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeMenuItem.AddRuler
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeMenuItem.RemoveRuler
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeMenuItem.ToggleDimensionType
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeMenuItem.TogglePercentVisibility
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeMenuItem.ToggleSettings
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class RulerModeViewModel(
    private val storage: SharedPreferences,
    isNightModeState: StateFlow<Boolean>,
) : ToolkitModeViewModel<RulerModeMenuItem>() {

    private val unpinnedPositionState = MutableStateFlow(Float.NaN)
    private val pinnedPositionsState = MutableStateFlow(emptyList<Float>())
    private val orientationState = MutableStateFlow(Orientation.HORIZONTAL)
    private val strokeScaleState = MutableStateFlow(storage.getFloat(KEY_STROKE_SCALE, DEFAULT_STROKE_SCALE))
    private val textScaleState = MutableStateFlow(storage.getFloat(KEY_TEXT_SCALE, DEFAULT_TEXT_SCALE))
    private val sensitivityState = MutableStateFlow(storage.getFloat(KEY_SENSITIVITY, DEFAULT_SENSITIVITY))

    private val dayStrokeColorState = MutableStateFlow(storage.getColor(KEY_DAY_STROKE_COLOR, DEFAULT_DAY_STROKE_COLOR))
    private val dayTextColorState = MutableStateFlow(storage.getColor(KEY_DAY_TEXT_COLOR, DEFAULT_DAY_TEXT_COLOR))
    private val nightStrokeColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_STROKE_COLOR, DEFAULT_NIGHT_STROKE_COLOR))
    private val nightTextColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_TEXT_COLOR, DEFAULT_NIGHT_TEXT_COLOR))

    private val dimensionTypeState = MutableStateFlow(DimensionType.DP)
    private val isPercentVisibleState = MutableStateFlow(false)
    private val areSettingsShownState = MutableStateFlow(false)

    override val menuItems: StateFlow<List<ToolkitBarMenuItem>> = combine(
        pinnedPositionsState,
        dimensionTypeState,
        isPercentVisibleState,
        areSettingsShownState,
        ::makeMenuItemList,
    ).stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed()
    )

    override val onMenuItemClick: (RulerModeMenuItem) -> Unit = OnMenuItemClickListener()
    override val onBackPressedCallback: OnBackPressedCallback = OnBackPressClickListener()

    val unpinnedPosition: StateFlow<Float> = unpinnedPositionState.asStateFlow()
    val pinnedPositions: StateFlow<List<Float>> = pinnedPositionsState.asStateFlow()
    val orientation: StateFlow<Orientation> = orientationState.asStateFlow()
    val strokeScale: StateFlow<Float> = strokeScaleState.asStateFlow()
    val textScale: StateFlow<Float> = textScaleState.asStateFlow()
    val sensitivity: StateFlow<Float> = sensitivityState.asStateFlow()
    val dimensionType: StateFlow<DimensionType> = dimensionTypeState.asStateFlow()
    val isPercentVisible: StateFlow<Boolean> = isPercentVisibleState.asStateFlow()
    val areSettingsShown: StateFlow<Boolean> = areSettingsShownState.asStateFlow()

    val strokeColor: StateFlow<Color> = switchColors(isNightModeState, dayStrokeColorState, nightStrokeColorState)
    val textColor: StateFlow<Color> = switchColors(isNightModeState, dayTextColorState, nightTextColorState)

    val onUnpinnedPositionChange: (Float) -> Unit = {
        unpinnedPositionState.value = it
    }
    val onOrientationChange: (Orientation) -> Unit = {
        if (pinnedPositionsState.value.isEmpty()) {
            orientationState.value = it
            unpinnedPositionState.value = Float.NaN
        }
    }
    val onStrokeScaleChange: (Float) -> Unit = {
        strokeScaleState.value = it
    }
    val onTextScaleChange: (Float) -> Unit = {
        textScaleState.value = it
    }
    val onSensitivityChange: (Float) -> Unit = {
        sensitivityState.value = it
    }
    val onAreSettingsShownChange: (Boolean) -> Unit = {
        areSettingsShownState.value = it
    }

    val onStrokeColorChange: (Color) -> Unit = updateColors(isNightModeState, dayStrokeColorState, nightStrokeColorState)
    val onTextColorChange: (Color) -> Unit = updateColors(isNightModeState, dayTextColorState, nightTextColorState)

    init {
        updateBackPressListenerEnabledState()
        observeAndSaveChanges()
    }

    private fun updateBackPressListenerEnabledState() = viewModelScope.launchUnit {
        combine(
            pinnedPositionsState,
            areSettingsShownState,
        ) { pinnedPositions, areSettingsShown ->
            when {
                pinnedPositions.isNotEmpty() -> {
                    true
                }
                areSettingsShown -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
            .collectLatest {
                onBackPressedCallback.isEnabled = it
            }
    }

    private fun observeAndSaveChanges() {
        updateFloat(storage, KEY_STROKE_SCALE, strokeScaleState)
        updateFloat(storage, KEY_TEXT_SCALE, textScaleState)
        updateFloat(storage, KEY_SENSITIVITY, sensitivityState)

        updateColor(storage, KEY_DAY_STROKE_COLOR, dayStrokeColorState)
        updateColor(storage, KEY_DAY_TEXT_COLOR, dayTextColorState)
        updateColor(storage, KEY_NIGHT_STROKE_COLOR, nightStrokeColorState)
        updateColor(storage, KEY_NIGHT_TEXT_COLOR, nightTextColorState)
    }

    private fun makeMenuItemList(
        pinnedPositions: List<Float>,
        dimensionType: DimensionType,
        isPercentVisible: Boolean,
        areSettingsShown: Boolean
    ): List<ToolkitBarMenuItem> {
        return listOf(
            ToggleDimensionType(
                text = dimensionType.name
            ),
            TogglePercentVisibility(
                isChecked = isPercentVisible
            ),
            AddRuler(),
            RemoveRuler(
                isEnabled = pinnedPositions.isNotEmpty(),
            ),
            ToggleSettings(
                isChecked = areSettingsShown,
            ),
        )
    }

    private fun toggleDimensionType() {
        dimensionTypeState.value = when (dimensionTypeState.value) {
            DimensionType.DP -> DimensionType.PX
            DimensionType.PX -> DimensionType.DP
        }
    }

    private fun removeLastRuler() {
        val pinnedPositions = pinnedPositionsState.value.toMutableList()
        unpinnedPositionState.value = pinnedPositions.removeLast()
        pinnedPositionsState.value = pinnedPositions
    }

    private fun addNewRuler() {
        val pinnedPositions = pinnedPositionsState.value.toMutableList()
        pinnedPositions.add(unpinnedPositionState.value)
        pinnedPositionsState.value = pinnedPositions
    }

    private fun togglePercentVisibility() {
        isPercentVisibleState.value = !isPercentVisibleState.value
    }

    private fun toggleSettingsShown() {
        areSettingsShownState.value = !areSettingsShownState.value
    }

    private inner class OnBackPressClickListener : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when {
                areSettingsShownState.value -> {
                    toggleSettingsShown()
                }
                pinnedPositionsState.value.isNotEmpty() -> {
                    removeLastRuler()
                }
            }
        }
    }

    private inner class OnMenuItemClickListener : (RulerModeMenuItem) -> Unit {
        override fun invoke(menuItem: RulerModeMenuItem) {
            exhaustive..when (menuItem) {
                is AddRuler -> {
                    addNewRuler()
                }
                is RemoveRuler -> {
                    removeLastRuler()
                }
                is ToggleDimensionType -> {
                    toggleDimensionType()
                }
                is TogglePercentVisibility -> {
                    togglePercentVisibility()
                }
                is ToggleSettings -> {
                    toggleSettingsShown()
                }
            }
        }
    }

    companion object {

        private const val KEY_STROKE_SCALE = "ruler_stroke_scale"
        private const val KEY_TEXT_SCALE = "ruler_text_scale"
        private const val KEY_SENSITIVITY = "ruler_sensitivity"

        private const val KEY_DAY_STROKE_COLOR = "ruler_day_stroke_color"
        private const val KEY_DAY_TEXT_COLOR = "ruler_day_text_color"

        private const val KEY_NIGHT_STROKE_COLOR = "ruler_night_stroke_color"
        private const val KEY_NIGHT_TEXT_COLOR = "ruler_night_text_color"

        private const val DEFAULT_STROKE_SCALE = 1.0f
        private const val DEFAULT_TEXT_SCALE = 1.0f
        private const val DEFAULT_SENSITIVITY = 1.0f

        private val DEFAULT_DAY_STROKE_COLOR = Color.Red
        private val DEFAULT_DAY_TEXT_COLOR = Color.White

        private val DEFAULT_NIGHT_STROKE_COLOR = Color.Red
        private val DEFAULT_NIGHT_TEXT_COLOR = Color.White
    }
}