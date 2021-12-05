package com.github.iojjj.bootstrap.qatoolkit.grid

import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.coroutines.launchUnit
import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.getColor
import com.github.iojjj.bootstrap.qatoolkit.grid.GridModeMenuItem.ToggleHorizontalSnapPosition
import com.github.iojjj.bootstrap.qatoolkit.grid.GridModeMenuItem.ToggleSettings
import com.github.iojjj.bootstrap.qatoolkit.grid.GridModeMenuItem.ToggleVerticalSnapPosition
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GridModeViewModel(
    private val storage: SharedPreferences,
    isNightModeState: StateFlow<Boolean>,
) : ToolkitModeViewModel<GridModeMenuItem>() {

    private val strokeScaleState = MutableStateFlow(storage.getFloat(KEY_STROKE_SCALE, DEFAULT_STROKE_SCALE))
    private val gridSizeState = MutableStateFlow(storage.getInt(KEY_GRID_SIZE, DEFAULT_GRID_SIZE))
    private val cellSizeState = MutableStateFlow(storage.getInt(KEY_CELL_SIZE, DEFAULT_CELL_SIZE))
    private val horizontalSnapPositionState = MutableStateFlow(SnapPosition.NONE)
    private val verticalSnapPositionState = MutableStateFlow(SnapPosition.NONE)
    private val areSettingsShownState = MutableStateFlow(false)

    private val dayStrokeColorState = MutableStateFlow(storage.getColor(KEY_DAY_STROKE_COLOR, DEFAULT_DAY_STROKE_COLOR))
    private val nightStrokeColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_STROKE_COLOR, DEFAULT_NIGHT_STROKE_COLOR))

    override val menuItems: StateFlow<List<ToolkitBarMenuItem>> = combine(
        horizontalSnapPositionState,
        verticalSnapPositionState,
        areSettingsShownState,
        ::makeMenuItems,
    ).stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed()
    )

    override val onMenuItemClick: (GridModeMenuItem) -> Unit = OnMenuItemClickListener()
    override val onBackPressedCallback: OnBackPressedCallback = OnBackPressListener()

    val strokeScale: StateFlow<Float> = strokeScaleState.asStateFlow()
    val gridSize: StateFlow<Int> = gridSizeState.asStateFlow()
    val cellSize: StateFlow<Int> = cellSizeState.asStateFlow()
    val horizontalSnapPosition: StateFlow<SnapPosition> = horizontalSnapPositionState.asStateFlow()
    val verticalSnapPosition: StateFlow<SnapPosition> = verticalSnapPositionState.asStateFlow()
    val areSettingsShown: StateFlow<Boolean> = areSettingsShownState.asStateFlow()
    val strokeColor: StateFlow<Color> = switchColors(isNightModeState, dayStrokeColorState, nightStrokeColorState)

    val onStrokeScaleChange: (value: Float) -> Unit = { value ->
        strokeScaleState.value = value
    }

    val onStrokeColorChange: (value: Color) -> Unit = updateColors(isNightModeState, dayStrokeColorState, nightStrokeColorState)

    val onGridSizeChange: (value: Int) -> Unit = { value ->
        gridSizeState.value = value
    }

    val onCellSizeChange: (value: Int) -> Unit = { value ->
        cellSizeState.value = value
    }
    val onAreSettingsShownChange: (Boolean) -> Unit = {
        areSettingsShownState.value = it
    }

    init {
        updateBackPressListenerEnabledState()
        observeAndSaveChanges()
    }

    private fun updateBackPressListenerEnabledState() = viewModelScope.launchUnit {
        areSettingsShownState
            .collectLatest {
                onBackPressedCallback.isEnabled = it
            }
    }

    private fun observeAndSaveChanges() {
        updateFloat(storage, KEY_STROKE_SCALE, strokeScaleState)
        updateInt(storage, KEY_GRID_SIZE, gridSizeState)
        updateInt(storage, KEY_CELL_SIZE, cellSizeState)
        updateColor(storage, KEY_DAY_STROKE_COLOR, dayStrokeColorState)
        updateColor(storage, KEY_NIGHT_STROKE_COLOR, nightStrokeColorState)
    }

    private fun makeMenuItems(
        horizontalSnapPosition: SnapPosition,
        verticalSnapPosition: SnapPosition,
        areSettingsShown: Boolean,
    ): List<ToolkitBarMenuItem> {
        return listOf(
            ToggleHorizontalSnapPosition(
                icon = when (horizontalSnapPosition) {
                    SnapPosition.NONE -> R.drawable.qatoolkit_grid_ic_align_horizontal_none_24dp
                    SnapPosition.START -> R.drawable.qatoolkit_grid_ic_align_horizontal_left_24dp
                    SnapPosition.END -> R.drawable.qatoolkit_grid_ic_align_horizontal_right_24dp
                }
            ),
            ToggleVerticalSnapPosition(
                icon = when (verticalSnapPosition) {
                    SnapPosition.NONE -> R.drawable.qatoolkit_grid_ic_align_vertical_none_24dp
                    SnapPosition.START -> R.drawable.qatoolkit_grid_ic_align_vertical_top_24dp
                    SnapPosition.END -> R.drawable.qatoolkit_grid_ic_align_vertical_bottom_24dp
                }
            ),
            ToggleSettings(
                isChecked = areSettingsShown
            )
        )
    }

    private fun toggleSettingsShown() {
        areSettingsShownState.value = !areSettingsShownState.value
    }

    private fun toggleSnapPosition(state: MutableStateFlow<SnapPosition>) {
        state.value = when (state.value) {
            SnapPosition.NONE -> SnapPosition.START
            SnapPosition.START -> SnapPosition.END
            SnapPosition.END -> SnapPosition.NONE
        }
    }

    private inner class OnBackPressListener : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (areSettingsShownState.value) {
                toggleSettingsShown()
            }
        }
    }

    private inner class OnMenuItemClickListener : (GridModeMenuItem) -> Unit {
        override fun invoke(menuItem: GridModeMenuItem) {
            exhaustive..when (menuItem) {
                is ToggleHorizontalSnapPosition -> {
                    toggleSnapPosition(horizontalSnapPositionState)
                }
                is ToggleVerticalSnapPosition -> {
                    toggleSnapPosition(verticalSnapPositionState)
                }
                is ToggleSettings -> {
                    toggleSettingsShown()
                }
            }
        }
    }

    companion object {

        private const val KEY_STROKE_SCALE = "grid_stroke_scale"
        private const val KEY_GRID_SIZE = "grid_grid_size"
        private const val KEY_CELL_SIZE = "grid_cell_size"

        private const val KEY_DAY_STROKE_COLOR = "grid_day_stroke_color"
        private const val KEY_NIGHT_STROKE_COLOR = "grid_night_stroke_color"

        private const val DEFAULT_STROKE_SCALE = 0.5f
        private const val DEFAULT_GRID_SIZE = 5
        private const val DEFAULT_CELL_SIZE = 8

        private val DEFAULT_DAY_STROKE_COLOR = Color.Red.copy(alpha = 0.5f)
        private val DEFAULT_NIGHT_STROKE_COLOR = Color.White.copy(alpha = 0.5f)
    }
}