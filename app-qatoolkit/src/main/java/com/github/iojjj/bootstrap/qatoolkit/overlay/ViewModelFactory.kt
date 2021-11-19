package com.github.iojjj.bootstrap.qatoolkit.overlay

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.iojjj.bootstrap.qatoolkit.grid.GridModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.initial.InitialModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarViewModel
import kotlinx.coroutines.flow.StateFlow

@Suppress("unchecked_cast")
class ViewModelFactory(
    private val context: Context,
    private val navigateTo: (ToolkitMode) -> Unit,
    private val sharedPreferences: SharedPreferences,
    private val isNightModeState: StateFlow<Boolean>,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            InitialModeViewModel::class.java -> InitialModeViewModel(navigateTo) as T
            InspectorModeViewModel::class.java -> InspectorModeViewModel(context, sharedPreferences, isNightModeState) as T
            GridModeViewModel::class.java -> GridModeViewModel(sharedPreferences, isNightModeState) as T
            RulerModeViewModel::class.java -> RulerModeViewModel(sharedPreferences, isNightModeState) as T
            ToolkitBarViewModel::class.java -> ToolkitBarViewModel(sharedPreferences) as T
            else -> error("Impossible route.")
        }
    }
}