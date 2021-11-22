package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.compose.runtime.Stable

@Stable
sealed interface ToolkitBarMenuItem {

    val isEnabled: Boolean

    @Stable
    interface IconOnly : ToolkitBarMenuItem,
        HasIcon {

        companion object {

            @Stable
            val None: IconOnly = MenuItemNone
        }
    }

    @Stable
    interface IconText : ToolkitBarMenuItem,
        HasIcon,
        HasText {

        companion object {

            @Stable
            val None: IconText = MenuItemNone
        }
    }

    @Stable
    interface TextOnly : ToolkitBarMenuItem,
        HasText {
        companion object {

            @Stable
            val None: TextOnly = MenuItemNone
        }
    }

    companion object {
        @Stable
        private object MenuItemNone :
            IconText,
            IconOnly,
            TextOnly {

            override val icon: Int = 0
            override val text: String = ""
            override val isEnabled: Boolean = false
        }
    }
}