package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple

import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property

@RequiresApi(Build.VERSION_CODES.M)
internal open class Api23RippleDrawableAttributeInspector : Api21RippleDrawableAttributeInspector() {

    override fun inspectRadius(drawable: RippleDrawable): Property<RippleRadius> {
        return Property.Known(
            drawable.radius.takeUnless { it == RippleDrawable.RADIUS_AUTO }
                ?.let { RippleRadius.Fixed(it.toFloat()) }
                ?: RippleRadius.Auto
        )
    }
}