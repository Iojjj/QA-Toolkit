package com.github.iojjj.bootstrap.qatoolkit.grid

import androidx.compose.runtime.Immutable
import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsMenuItem

@Immutable
sealed interface GridSettingsMenuItem : SettingsMenuItem {

    object StrokeScale : GridSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_grid_settings_item_stroke_scale
        override val icon: Int = R.drawable.qatoolkit_grid_ic_stroke_scale_24dp
    }

    object StrokeColor : GridSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_grid_settings_item_stroke_color
        override val icon: Int = R.drawable.qatoolkit_grid_ic_stroke_color_24dp
    }

    object GridSize : GridSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_grid_settings_item_grid_size
        override val icon: Int = R.drawable.qatoolkit_grid_ic_grid_size_24dp
    }

    object CellSize : GridSettingsMenuItem {
        override val title: Int = R.string.qatoolkit_grid_settings_item_cell_size
        override val icon: Int = R.drawable.qatoolkit_grid_ic_cell_size_24dp
    }
}