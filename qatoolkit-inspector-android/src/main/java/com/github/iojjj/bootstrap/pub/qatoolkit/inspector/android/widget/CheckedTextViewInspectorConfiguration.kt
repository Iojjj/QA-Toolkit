package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.widget.CheckedTextView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.CATEGORY_ORDER_TEXT_VIEW_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [CheckedTextView] type.
 */
class CheckedTextViewInspectorConfiguration : InspectorConfiguration<CheckedTextView> {

    override fun configure(): Iterable<CategoryInspector<CheckedTextView>> = Inspect {
        CheckedTextViewCategory {
            TintedDrawable(ATTRIBUTE_CHECK_MARK_DRAWABLE) { it.checkMarkDrawable to it.checkMarkTintList }
        }
    }

    companion object {

        const val CATEGORY_CHECKED_TEXT: String = "Checked Text"

        // TODO Reorder from text view
        const val CATEGORY_ORDER_CHECKED_TEXT_FIRST: Int = CATEGORY_ORDER_TEXT_VIEW_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_CHECKED_TEXT: Int = CATEGORY_ORDER_CHECKED_TEXT_FIRST
        const val CATEGORY_ORDER_CHECKED_TEXT_LAST: Int = CATEGORY_ORDER_CHECKED_TEXT

        const val ATTRIBUTE_CHECK_MARK_DRAWABLE: String = "Check mark drawable"

        fun <T> InspectorBuilder<T>.CheckedTextViewCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_CHECKED_TEXT, CATEGORY_ORDER_CHECKED_TEXT, allowNulls, block)
        }
    }
}