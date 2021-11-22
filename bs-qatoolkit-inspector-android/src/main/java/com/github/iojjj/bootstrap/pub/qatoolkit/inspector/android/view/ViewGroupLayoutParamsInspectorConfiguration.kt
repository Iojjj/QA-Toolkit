package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.view

import android.view.ViewGroup
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Lens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens.ViewGroupLayoutParamsLens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewLayoutCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.LayoutDimension

/**
 * Implementation of [InspectorConfiguration] for [LayoutParams][ViewGroup.LayoutParams] type.
 */
// Executing before View's inspector
@Order(-1 * INSPECTOR_ORDER_STEP)
@Lens(ViewGroupLayoutParamsLens::class)
class ViewGroupLayoutParamsInspectorConfiguration : InspectorConfiguration<ViewGroup.LayoutParams> {

    override fun configure(): Iterable<CategoryInspector<ViewGroup.LayoutParams>> = Inspect {
        ViewLayoutCategory {
            LayoutDimension(ATTRIBUTE_LAYOUT_WIDTH) { it.width }
            LayoutDimension(ATTRIBUTE_LAYOUT_HEIGHT) { it.height }
        }
    }

    companion object {

        const val ATTRIBUTE_LAYOUT_WIDTH: String = "Layout params: width"
        const val ATTRIBUTE_LAYOUT_HEIGHT: String = "Layout params: height"
    }
}
