package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.graphics.Typeface
import android.os.Build
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Lens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens.TypefaceAttributeLens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.CATEGORY_ORDER_TEXT_VIEW_TEXT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [Typeface] type.
 */
@Lens(TypefaceAttributeLens::class)
@Order(1 * INSPECTOR_ORDER_STEP + 1)
class TextViewTypefaceInspectorConfiguration : InspectorConfiguration<Typeface> {

    override fun configure(): Iterable<CategoryInspector<Typeface>> = Inspect {
        TextViewFontCategory {
            Boolean(ATTRIBUTE_TEXT_IS_BOLD) { it.isBold }
            Boolean(ATTRIBUTE_TEXT_IS_ITALIC) { it.isItalic }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Int(ATTRIBUTE_TEXT_WEIGHT) { it.weight }
            } else {
                ApiRestricted(ATTRIBUTE_TEXT_WEIGHT, Build.VERSION_CODES.P)
            }
        }
    }

    companion object {

        const val CATEGORY_TEXT_FONT: String = "Text: Font"

        const val CATEGORY_ORDER_FIRST: Int = CATEGORY_ORDER_TEXT_VIEW_TEXT + 100
        const val CATEGORY_ORDER_FONT: Int = CATEGORY_ORDER_FIRST
        const val CATEGORY_ORDER_LAST: Int = CATEGORY_ORDER_FONT

        const val ATTRIBUTE_TEXT_IS_BOLD: String = "Is font bold"
        const val ATTRIBUTE_TEXT_IS_ITALIC: String = "Is font italic"
        const val ATTRIBUTE_TEXT_WEIGHT: String = "Font weight"

        fun <T> InspectorBuilder<T>.TextViewFontCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_FONT, CATEGORY_ORDER_FONT, allowNulls, block)
        }
    }
}