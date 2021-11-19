package com.github.iojjj.bootstrap.qatoolkit.grid

import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.Checkable
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem

sealed interface GridModeMenuItem {

    data class ToggleHorizontalSnapPosition(
        override val icon: Int = R.drawable.qatoolkit_grid_ic_align_horizontal_none_24dp,
        override val isEnabled: Boolean = true,
    ) : GridModeMenuItem,
        ToolkitBarMenuItem.IconOnly

    data class ToggleVerticalSnapPosition(
        override val icon: Int = R.drawable.qatoolkit_grid_ic_align_vertical_none_24dp,
        override val isEnabled: Boolean = true,
    ) : GridModeMenuItem,
        ToolkitBarMenuItem.IconOnly

    data class ToggleSettings(
        override val icon: Int = R.drawable.qatoolkit_common_ic_settings_24dp,
        override val isEnabled: Boolean = true,
        override val isChecked: Boolean = false,
    ) : GridModeMenuItem,
        ToolkitBarMenuItem.IconOnly,
        Checkable
}