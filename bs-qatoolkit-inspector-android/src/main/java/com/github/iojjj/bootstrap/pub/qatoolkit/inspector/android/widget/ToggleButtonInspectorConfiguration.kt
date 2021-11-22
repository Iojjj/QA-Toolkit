package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.widget.ToggleButton
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.CompoundButtonInspectorConfiguration.Companion.CATEGORY_ORDER_COMPOUND_BUTTON_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [ToggleButton] type.
 */
class ToggleButtonInspectorConfiguration : InspectorConfiguration<ToggleButton> {

    override fun configure(): Iterable<CategoryInspector<ToggleButton>> = Inspect {
        ToggleButtonTextCategory {
            String(ATTRIBUTE_TEXT_ON) { it.textOn }
            String(ATTRIBUTE_TEXT_OFF) { it.textOff }
        }
    }

    companion object {

        const val CATEGORY_TOGGLE_BUTTON_TEXT: String = "Toggle Button: Text"

        const val CATEGORY_ORDER_TOGGLE_BUTTON_FIRST: Int = CATEGORY_ORDER_COMPOUND_BUTTON_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_TOGGLE_BUTTON_TEXT: Int = CATEGORY_ORDER_TOGGLE_BUTTON_FIRST
        const val CATEGORY_ORDER_TOGGLE_BUTTON_LAST: Int = CATEGORY_ORDER_TOGGLE_BUTTON_TEXT

        const val ATTRIBUTE_TEXT_ON: String = "Text for ON state"
        const val ATTRIBUTE_TEXT_OFF: String = "Text for OFF state"

        fun <T> InspectorBuilder<T>.ToggleButtonTextCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TOGGLE_BUTTON_TEXT, CATEGORY_ORDER_TOGGLE_BUTTON_TEXT, allowNulls, block)
        }
    }
}