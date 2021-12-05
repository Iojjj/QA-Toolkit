package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.view.View
import android.widget.Checkable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_CHECKABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_CHECKED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewStateCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean

/**
 * Implementation of [InspectorConfiguration] for [Checkable] type.
 */
// Executing after View's inspector
@Order(1 * INSPECTOR_ORDER_STEP)
class CheckableInspectorConfiguration : InspectorConfiguration<View> {

    // Type of inspectable object set to View because attributes must be presented for any view
    // no matter if it implements Checkable interface or not.
    override fun configure(): Iterable<CategoryInspector<View>> = Inspect {
        ViewStateCategory {
            Boolean(ATTRIBUTE_CHECKABLE) { it is Checkable }
            Boolean(ATTRIBUTE_CHECKED) { it is Checkable && it.isChecked }
        }
    }
}