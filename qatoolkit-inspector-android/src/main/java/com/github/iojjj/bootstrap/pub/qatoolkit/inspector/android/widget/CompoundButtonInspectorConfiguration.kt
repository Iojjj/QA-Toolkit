package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.CompoundButton
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.CATEGORY_ORDER_TEXT_VIEW_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [CompoundButton] type.
 */
class CompoundButtonInspectorConfiguration : InspectorConfiguration<CompoundButton> {

    override fun configure(): Iterable<CategoryInspector<CompoundButton>> = Inspect {
        CompoundButtonCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TintedDrawable(ATTRIBUTE_CHECK_MARK_DRAWABLE) { it.buttonDrawable to it.buttonTintList }
            } else {
                ApiRestricted(ATTRIBUTE_CHECK_MARK_DRAWABLE, Build.VERSION_CODES.M)
            }
        }
    }

    companion object {

        const val CATEGORY_COMPOUND_BUTTON: String = "Compound Button"

        // TODO Reorder from text view
        const val CATEGORY_ORDER_COMPOUND_BUTTON_FIRST: Int = CATEGORY_ORDER_TEXT_VIEW_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_COMPOUND_BUTTON: Int = CATEGORY_ORDER_COMPOUND_BUTTON_FIRST
        const val CATEGORY_ORDER_COMPOUND_BUTTON_LAST: Int = CATEGORY_ORDER_COMPOUND_BUTTON

        const val ATTRIBUTE_CHECK_MARK_DRAWABLE: String = "Compound button drawable"

        fun <T> InspectorBuilder<T>.CompoundButtonCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_COMPOUND_BUTTON, CATEGORY_ORDER_COMPOUND_BUTTON, allowNulls, block)
        }
    }
}