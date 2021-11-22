package com.github.iojjj.bootstrap.qatoolkit.ruler

import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsMenuItem

sealed interface RulerSettingsMenuItem : SettingsMenuItem {

    object StrokeScale : RulerSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_ruler_settings_item_stroke_scale
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_stroke_scale_24dp
    }

    object StrokeColor : RulerSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_ruler_settings_item_stroke_color
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_stroke_color_24dp
    }

    object TextScale : RulerSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_ruler_settings_item_text_scale
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_text_scale_24dp
    }

    object TextColor : RulerSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_ruler_settings_item_text_color
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_text_color_24dp
    }

    object Sensitivity : RulerSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_ruler_settings_item_sensitivity
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_sensitivity_24dp
    }
}