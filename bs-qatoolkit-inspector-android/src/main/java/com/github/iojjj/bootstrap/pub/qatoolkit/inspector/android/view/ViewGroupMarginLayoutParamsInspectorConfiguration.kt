package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.view

import android.view.ViewGroup
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Lens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens.ViewGroupMarginLayoutParamsLens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewLayoutCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.LayoutDimension

/**
 * Implementation of [InspectorConfiguration] for [MarginLayoutParams][ViewGroup.MarginLayoutParams] type.
 */
// Executing after ViewGroup's inspector
@Order(1 * INSPECTOR_ORDER_STEP)
@Lens(ViewGroupMarginLayoutParamsLens::class)
class ViewGroupMarginLayoutParamsInspectorConfiguration : InspectorConfiguration<ViewGroup.MarginLayoutParams> {

    override fun configure(): Iterable<CategoryInspector<ViewGroup.MarginLayoutParams>> = Inspect {
        ViewLayoutCategory {
            LayoutDimension(ATTRIBUTE_MARGIN_START) { it.marginStart }
            LayoutDimension(ATTRIBUTE_MARGIN_TOP) { it.topMargin }
            LayoutDimension(ATTRIBUTE_MARGIN_END) { it.marginEnd }
            LayoutDimension(ATTRIBUTE_MARGIN_BOTTOM) { it.bottomMargin }
        }
    }

    companion object {

        const val ATTRIBUTE_MARGIN_START: String = "Start margin"
        const val ATTRIBUTE_MARGIN_TOP: String = "Top margin"
        const val ATTRIBUTE_MARGIN_END: String = "End margin"
        const val ATTRIBUTE_MARGIN_BOTTOM: String = "Bottom margin"
    }
}