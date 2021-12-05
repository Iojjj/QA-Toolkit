package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple

import android.annotation.SuppressLint
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple.RippleDrawableAttributeInspector.Companion.RIPPLE_DRAWABLE_STATE_FIELD_COLOR
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple.RippleDrawableAttributeInspector.Companion.RIPPLE_DRAWABLE_STATE_FIELD_MAX_RADIUS
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple.RippleDrawableAttributeInspector.Companion.field

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal open class Api21RippleDrawableAttributeInspector : RippleDrawableAttributeInspector {

    override fun inspectColor(drawable: RippleDrawable): Property<Int?> {
        val drawableState = drawable.constantState!!
        return when (val colorProperty = drawableState.field(RIPPLE_DRAWABLE_STATE_FIELD_COLOR)) {
            is Property.Known -> {
                Property.Known(colorProperty.value?.defaultColor)
            }
            is Property.Unknown -> {
                Property.Unknown(colorProperty.api)
            }
        }
    }

    @SuppressLint("InlinedApi")
    override fun inspectRadius(drawable: RippleDrawable): Property<RippleRadius> {
        val drawableState = drawable.constantState!!
        return when (val maxRadiusProperty = drawableState.field(RIPPLE_DRAWABLE_STATE_FIELD_MAX_RADIUS)) {
            is Property.Known -> {
                Property.Known(
                    maxRadiusProperty.value.takeUnless { it == RippleDrawable.RADIUS_AUTO }
                        ?.let { RippleRadius.Fixed(it.toFloat()) }
                        ?: RippleRadius.Auto
                )
            }
            is Property.Unknown -> {
                Property.Unknown(maxRadiusProperty.api)
            }
        }
    }
}