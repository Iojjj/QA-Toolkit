package com.github.iojjj.bootstrap.qatoolkit.initial

import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem

sealed interface InitialModeMenuItem {

    object InspectMode : InitialModeMenuItem,
        ToolkitBarMenuItem.IconOnly {

        override val icon: Int = R.drawable.qatoolkit_initial_ic_inspector_24dp
        override val isEnabled: Boolean = true
    }

    object GridMode : InitialModeMenuItem,
        ToolkitBarMenuItem.IconOnly {

        override val icon: Int = R.drawable.qatoolkit_initial_ic_grid_24dp
        override val isEnabled: Boolean = true
    }

    object RulerMode : InitialModeMenuItem,
        ToolkitBarMenuItem.IconOnly {

        override val icon: Int = R.drawable.qatoolkit_initial_ic_ruler_24dp
        override val isEnabled: Boolean = true
    }
}