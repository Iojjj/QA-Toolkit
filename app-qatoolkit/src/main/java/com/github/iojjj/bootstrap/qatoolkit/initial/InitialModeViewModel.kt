package com.github.iojjj.bootstrap.qatoolkit.initial

import androidx.activity.OnBackPressedCallback
import com.github.iojjj.bootstrap.qatoolkit.initial.InitialModeMenuItem.GridMode
import com.github.iojjj.bootstrap.qatoolkit.initial.InitialModeMenuItem.InspectMode
import com.github.iojjj.bootstrap.qatoolkit.initial.InitialModeMenuItem.RulerMode
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitMode
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InitialModeViewModel(
    private val navigateTo: (ToolkitMode) -> Unit,
) : ToolkitModeViewModel<InitialModeMenuItem>() {

    override val menuItems: StateFlow<List<ToolkitBarMenuItem>> = MutableStateFlow(
        listOf(
            InspectMode,
            GridMode,
            RulerMode,
        )
    ).asStateFlow()

    override val onMenuItemClick: (InitialModeMenuItem) -> Unit = { menuItem ->
        navigateTo(
            when (menuItem) {
                is InspectMode -> {
                    ToolkitMode.Inspect
                }
                is GridMode -> {
                    ToolkitMode.Grid
                }
                is RulerMode -> {
                    ToolkitMode.Ruler
                }
            }
        )
    }

    override val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            /* no-op */
        }
    }
}