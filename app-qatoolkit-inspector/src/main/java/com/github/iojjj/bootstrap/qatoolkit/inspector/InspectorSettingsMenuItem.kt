package com.github.iojjj.bootstrap.qatoolkit.inspector

import com.github.iojjj.bootstrap.qatoolkit.settings.HasText
import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsMenuItem

sealed interface InspectorSettingsMenuItem : SettingsMenuItem {

    object NodeStrokeScale : InspectorSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_node_stroke_scale
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_stroke_scale_24dp
    }

    object NodeStrokeColor : InspectorSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_node_stroke_color
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_stroke_color_24dp
    }

    object SelectedStrokeColor : InspectorSettingsMenuItem, HasText {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_selected_stroke_color
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_stroke_color_24dp
        override val text: String = "S"
    }

    object PinnedStrokeColor : InspectorSettingsMenuItem, HasText {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_pinned_stroke_color
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_stroke_color_24dp
        override val text: String = "P"
    }

    object TextScale : InspectorSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_text_scale
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_text_scale_24dp
    }

    object TextColor : InspectorSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_text_color
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_text_color_24dp
    }

    object OverlayColor : InspectorSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_overlay_color
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_overlay_color_24dp
    }

    object ArrowColor : InspectorSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_inspector_settings_item_arrow_color
        override val icon: Int = R.drawable.qatoolkit_inspector_ic_arrow_color_24dp
    }
}