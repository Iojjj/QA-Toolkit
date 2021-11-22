package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.appcompat.widget

import android.widget.CompoundButton
import androidx.core.widget.CompoundButtonCompat
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.CompoundButtonInspectorConfiguration.Companion.ATTRIBUTE_CHECK_MARK_DRAWABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.CompoundButtonInspectorConfiguration.Companion.CompoundButtonCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [CompoundButton] type.
 */
@Order(1 * INSPECTOR_ORDER_STEP)
class CompoundButtonCompatInspectorConfiguration : InspectorConfiguration<CompoundButton> {

    override fun configure(): Iterable<CategoryInspector<CompoundButton>> = Inspect {
        CompoundButtonCategory {
            TintedDrawable(ATTRIBUTE_CHECK_MARK_DRAWABLE) { CompoundButtonCompat.getButtonDrawable(it) to it.buttonTintList }
        }
    }
}