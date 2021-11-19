package com.github.iojjj.bootstrap.qatoolkit.inspector

import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.Checkable
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem

sealed interface InspectorModeMenuItem {

    data class PinNode(
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_pin_outlined_24dp,
        override val isEnabled: Boolean = true,
    ) : InspectorModeMenuItem,
        ToolkitBarMenuItem.IconOnly

    data class TogglePercentVisibility(
        override val text: String = "%",
        override val icon: Int = R.drawable.qatoolkit_common_ic_visible_24dp,
        override val isEnabled: Boolean = true,
        override val isChecked: Boolean = false,
    ) : InspectorModeMenuItem,
        ToolkitBarMenuItem.IconText,
        Checkable

    data class ToggleDimensionType(
        override val text: String = "DP",
        override val isEnabled: Boolean = true,
    ) : InspectorModeMenuItem,
        ToolkitBarMenuItem.TextOnly

    data class ToggleLayer(
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_layer_24dp,
        override val text: String = "",
        override val isEnabled: Boolean = true,
    ) : InspectorModeMenuItem,
        ToolkitBarMenuItem.IconText

    data class EnterInspectDetailsMode(
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_info_24dp,
        override val isEnabled: Boolean = true,
    ) : InspectorModeMenuItem,
        ToolkitBarMenuItem.IconOnly

    data class ToggleSettings(
        override val icon: Int = R.drawable.qatoolkit_common_ic_settings_24dp,
        override val isEnabled: Boolean = true,
        override val isChecked: Boolean = false,
    ) : InspectorModeMenuItem,
        ToolkitBarMenuItem.IconOnly,
        Checkable
}