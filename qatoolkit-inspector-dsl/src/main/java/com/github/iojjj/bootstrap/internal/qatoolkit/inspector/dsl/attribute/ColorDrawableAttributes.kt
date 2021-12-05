package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

import android.graphics.drawable.ColorDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute.Companion.color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string

internal fun colorDrawableAttributes(
    name: String,
    drawable: ColorDrawable,
): Sequence<Attribute> {
    return sequenceOf(
        string("$name: Type", "Color"),
        color("$name: Color", drawable.color),
    )
}