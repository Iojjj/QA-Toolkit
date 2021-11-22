package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.widget.AutoCompleteTextView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.CATEGORY_ORDER_TEXT_VIEW_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Drawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Id
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.LayoutDimension

/**
 * Implementation of [InspectorConfiguration] for [AutoCompleteTextView] type.
 */
class AutoCompleteTextViewInspectorConfiguration : InspectorConfiguration<AutoCompleteTextView> {

    override fun configure(): Iterable<CategoryInspector<AutoCompleteTextView>> = Inspect {
        AutoCompleteTextCompletionCategory {
            String(ATTRIBUTE_COMPLETION_HINT) { it.completionHint }
            Int(ATTRIBUTE_COMPLETION_THRESHOLD) { it.threshold }
        }
        AutoCompleteTextDropDownCategory {
            Id(ATTRIBUTE_DROP_DOWN_ANCHOR) { it.dropDownAnchor }
            Drawable(ATTRIBUTE_DROP_DOWN_BACKGROUND) { it.dropDownBackground }
            LayoutDimension(ATTRIBUTE_DROP_DOWN_WIDTH) { it.dropDownWidth }
            LayoutDimension(ATTRIBUTE_DROP_DOWN_HEIGHT) { it.dropDownHeight }
            Dimension(ATTRIBUTE_DROP_DOWN_HORIZONTAL_OFFSET) { it.dropDownHorizontalOffset }
            Dimension(ATTRIBUTE_DROP_DOWN_VERTICAL_OFFSET) { it.dropDownVerticalOffset }
        }
    }

    companion object {

        const val CATEGORY_AUTO_COMPLETE_TEXT_VIEW_COMPLETION: String = "Autocomplete Text: Completion"
        const val CATEGORY_AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN: String = "Autocomplete Text: Drop Down"

        const val CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_FIRST: Int = CATEGORY_ORDER_TEXT_VIEW_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_COMPLETION: Int = CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_FIRST
        const val CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN: Int =
            CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_COMPLETION + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_LAST: Int = CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN

        const val ATTRIBUTE_COMPLETION_HINT: String = "Optional hint text displayed at the bottom of the the matching list"
        const val ATTRIBUTE_COMPLETION_THRESHOLD: String = "Number of characters the user must type before the drop down list is shown"
        const val ATTRIBUTE_DROP_DOWN_ANCHOR: String = "Drop down anchor id"
        const val ATTRIBUTE_DROP_DOWN_BACKGROUND: String = "Drop down background"
        const val ATTRIBUTE_DROP_DOWN_WIDTH: String = "Drop down width"
        const val ATTRIBUTE_DROP_DOWN_HEIGHT: String = "Drop down height"
        const val ATTRIBUTE_DROP_DOWN_HORIZONTAL_OFFSET: String = "Drop down horizontal offset"
        const val ATTRIBUTE_DROP_DOWN_VERTICAL_OFFSET: String = "Drop down vertical offset"

        fun <T> InspectorBuilder<T>.AutoCompleteTextCompletionCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_AUTO_COMPLETE_TEXT_VIEW_COMPLETION, CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_COMPLETION, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.AutoCompleteTextDropDownCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN, CATEGORY_ORDER_AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN, allowNulls, block)
        }
    }
}