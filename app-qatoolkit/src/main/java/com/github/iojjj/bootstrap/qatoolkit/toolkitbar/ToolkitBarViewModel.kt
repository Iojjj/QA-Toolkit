package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import com.github.iojjj.bootstrap.qatoolkit.getEnum
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel.Companion.updateEnum
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel.Companion.updateFloat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToolkitBarViewModel(
    private val storage: SharedPreferences,
) : ViewModel() {

    private val offsetXState = MutableStateFlow(storage.getFloat(KEY_OFFSET_X, DEFAULT_OFFSET_X))
    private val offsetYState = MutableStateFlow(storage.getFloat(KEY_OFFSET_Y, DEFAULT_OFFSET_Y))
    private val orientationState = MutableStateFlow(storage.getEnum(KEY_ORIENTATION, DEFAULT_ORIENTATION))

    val offsetX: StateFlow<Float> = offsetXState.asStateFlow()
    val offsetY: StateFlow<Float> = offsetYState.asStateFlow()
    val orientation: StateFlow<Orientation> = orientationState.asStateFlow()

    val onOffsetXChange: (Float) -> Unit = {
        offsetXState.value = it
    }
    val onOffsetYChange: (Float) -> Unit = {
        offsetYState.value = it
    }
    val onOrientationChange: (Orientation) -> Unit = {
        orientationState.value = it
    }

    init {
        observeAndSaveChanges()
    }

    private fun observeAndSaveChanges() {
        updateFloat(storage, KEY_OFFSET_X, offsetXState)
        updateFloat(storage, KEY_OFFSET_Y, offsetYState)
        updateEnum(storage, KEY_ORIENTATION, orientationState)
    }

    companion object {

        private const val KEY_OFFSET_X = "toolkit_bar_offset_x"
        private const val KEY_OFFSET_Y = "toolkit_bar_offset_y"
        private const val KEY_ORIENTATION = "toolkit_bar_orientation"

        private const val DEFAULT_OFFSET_X = -1.0f
        private const val DEFAULT_OFFSET_Y = -1.0f
        private val DEFAULT_ORIENTATION = Orientation.VERTICAL
    }
}