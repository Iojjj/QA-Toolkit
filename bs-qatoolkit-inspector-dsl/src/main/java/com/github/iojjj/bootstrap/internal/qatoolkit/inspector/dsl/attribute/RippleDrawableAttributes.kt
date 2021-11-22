package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

import android.graphics.drawable.RippleDrawable
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple.RippleDrawableAttributeInspector
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple.RippleRadius
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute.Companion.color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.fold

internal fun rippleDrawableAttributes(
    name: String,
    drawable: RippleDrawable,
): Sequence<Attribute> {
    return sequenceOf(
        string("$name: Type", "Ripple"),
        RippleDrawableAttributeInspector.inspectColor(drawable).fold("$name: Color") { attribute, it ->
            it?.let { color(attribute, it) }
        },
        RippleDrawableAttributeInspector.inspectRadius(drawable).fold("$name: Radius") { attribute, value ->
            when (value) {
                RippleRadius.Auto -> string(attribute, "auto")
                is RippleRadius.Fixed -> dimension(attribute, value.value)
            }
        }
    ).filterNotNull()
}