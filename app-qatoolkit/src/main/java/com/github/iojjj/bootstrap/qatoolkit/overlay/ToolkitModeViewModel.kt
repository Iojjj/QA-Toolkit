package com.github.iojjj.bootstrap.qatoolkit.overlay

import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.putColor
import com.github.iojjj.bootstrap.qatoolkit.putEnum
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
abstract class ToolkitModeViewModel<T> : ViewModel() {

    @Stable
    abstract val menuItems: StateFlow<List<ToolkitBarMenuItem>>

    @Stable
    abstract val onMenuItemClick: (menuItem: T) -> Unit

    @Stable
    abstract val onBackPressedCallback: OnBackPressedCallback

    companion object {

        fun ViewModel.switchColors(
            isNightModeState: StateFlow<Boolean>,
            dayColorState: StateFlow<Color>,
            nightColorState: StateFlow<Color>
        ): StateFlow<Color> {
            return combine(isNightModeState, dayColorState, nightColorState, ::chooseColor).stateIn(
                scope = viewModelScope,
                initialValue = chooseColor(isNightModeState.value, dayColorState.value, nightColorState.value),
                started = SharingStarted.Lazily
            )
        }

        private fun chooseColor(
            isNightMode: Boolean,
            dayColor: Color,
            nightColor: Color
        ): Color {
            return when (isNightMode) {
                true -> nightColor
                false -> dayColor
            }
        }

        fun updateColors(
            isNightModeState: StateFlow<Boolean>,
            dayColorState: MutableStateFlow<Color>,
            nightColorState: MutableStateFlow<Color>,
        ): (Color) -> Unit = { color ->
            exhaustive..when (isNightModeState.value) {
                true -> nightColorState.value = color
                false -> dayColorState.value = color
            }
        }

        inline fun <T> ViewModel.updatePreferences(
            storage: SharedPreferences,
            flow: Flow<T>,
            crossinline save: SharedPreferences.Editor.(T) -> Unit
        ) {
            viewModelScope.launch {
                flow.collectLatest {
                    storage.edit {
                        save(it)
                    }
                }
            }
        }

        inline fun ViewModel.updateInt(
            storage: SharedPreferences,
            key: String,
            flow: Flow<Int>,
        ) {
            updatePreferences(storage, flow) {
                putInt(key, it)
            }
        }

        inline fun ViewModel.updateFloat(
            storage: SharedPreferences,
            key: String,
            flow: Flow<Float>,
        ) {
            updatePreferences(storage, flow) {
                putFloat(key, it)
            }
        }

        inline fun ViewModel.updateColor(
            storage: SharedPreferences,
            key: String,
            flow: Flow<Color>,
        ) {
            updatePreferences(storage, flow) {
                putColor(key, it)
            }
        }

        inline fun <T : Enum<T>> ViewModel.updateEnum(
            storage: SharedPreferences,
            key: String,
            flow: Flow<T>,
        ) {
            updatePreferences(storage, flow) {
                putEnum(key, it)
            }
        }
    }
}