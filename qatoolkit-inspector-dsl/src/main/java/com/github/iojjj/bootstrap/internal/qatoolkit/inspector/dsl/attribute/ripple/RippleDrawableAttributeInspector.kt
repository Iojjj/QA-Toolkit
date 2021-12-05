package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection.ReflectionApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection.ReflectionField

internal interface RippleDrawableAttributeInspector {

    fun inspectColor(drawable: RippleDrawable): Property<Int?>
    fun inspectRadius(drawable: RippleDrawable): Property<RippleRadius>

    companion object : RippleDrawableAttributeInspector by when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            Api23RippleDrawableAttributeInspector()
        }
        else -> {
            Api21RippleDrawableAttributeInspector()
        }
    } {

        private val RIPPLE_DRAWABLE_STATE_API by lazy {
            ReflectionApi("android.graphics.drawable.RippleDrawable\$RippleState")
        }

        val RIPPLE_DRAWABLE_STATE_FIELD_COLOR = ReflectionField.Key<ColorStateList?>(0, "mColor")
        val RIPPLE_DRAWABLE_STATE_FIELD_MAX_RADIUS = ReflectionField.Key<Int>(Build.VERSION_CODES.M, "mMaxRadius")

        fun <R> Drawable.ConstantState.field(key: ReflectionField.Key<R>): Property<R> {
            return RIPPLE_DRAWABLE_STATE_API
                ?.field(key)
                ?.let {
                    Property.Known(it.get(this))
                }
                ?: Property.Unknown(key.api)
        }
    }
}
