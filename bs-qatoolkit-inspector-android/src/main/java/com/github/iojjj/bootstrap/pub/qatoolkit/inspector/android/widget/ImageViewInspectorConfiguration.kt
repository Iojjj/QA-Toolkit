package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.widget.ImageView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [ImageView] type.
 */
class ImageViewInspectorConfiguration : InspectorConfiguration<ImageView> {

    override fun configure(): Iterable<CategoryInspector<ImageView>> = Inspect {
        ImageCategory {
            TintedDrawable(ATTRIBUTE_DRAWABLE) { it.drawable to it.imageTintList }
            String(ATTRIBUTE_SCALE_TYPE) { it.scaleType.name }
        }
    }

    companion object {

        const val CATEGORY_IMAGE: String = "Image"

        const val CATEGORY_ORDER_IMAGE_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND + CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_IMAGE: Int = CATEGORY_ORDER_IMAGE_FIRST
        const val CATEGORY_ORDER_IMAGE_LAST: Int = CATEGORY_ORDER_IMAGE

        const val ATTRIBUTE_DRAWABLE: String = "Image drawable"
        const val ATTRIBUTE_SCALE_TYPE: String = "Scale type"

        fun <T> InspectorBuilder<T>.ImageCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_IMAGE, CATEGORY_ORDER_IMAGE, allowNulls, block)
        }
    }
}