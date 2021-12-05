package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

import android.content.res.ColorStateList
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Corners
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Shape
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection.ReflectionApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection.ReflectionField.Key

internal interface GradientDrawableAttributeInspector {

    fun inspectType(drawable: GradientDrawable): Property<GradientType>
    fun inspectCenter(drawable: GradientDrawable): Property<PointF>
    fun getCorners(drawable: GradientDrawable): Property<Corners>
    fun inspectColors(drawable: GradientDrawable): Property<GradientColors>
    fun inspectShape(drawable: GradientDrawable): Property<Shape>
    fun getRectangleShape(drawable: GradientDrawable): Shape
    fun getRingShape(drawable: GradientDrawable): Shape
    fun inspectUseLevel(drawable: GradientDrawable): Property<Boolean>

    companion object : GradientDrawableAttributeInspector by when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            Api29GradientDrawableAttributeInspector()
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Api24GradientDrawableAttributeInspector()
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            Api23GradientDrawableAttributeInspector()
        }
        else -> {
            Api21GradientDrawableAttributeInspector()
        }
    } {

        private val GRADIENT_DRAWABLE_STATE_API by lazy {
            ReflectionApi("android.graphics.drawable.GradientDrawable\$GradientState")
        }

        val GRADIENT_DRAWABLE_STATE_FIELD_GRADIENT = Key<Int>(Build.VERSION_CODES.N, "mGradient")
        val GRADIENT_DRAWABLE_STATE_FIELD_INNER_RADIUS = Key<Int>(Build.VERSION_CODES.Q, "mInnerRadius")
        val GRADIENT_DRAWABLE_STATE_FIELD_INNER_RADIUS_RATIO = Key<Float>(Build.VERSION_CODES.Q, "mInnerRadiusRatio")
        val GRADIENT_DRAWABLE_STATE_FIELD_THICKNESS = Key<Int>(Build.VERSION_CODES.Q, "mThickness")
        val GRADIENT_DRAWABLE_STATE_FIELD_THICKNESS_RATIO = Key<Float>(Build.VERSION_CODES.Q, "mThicknessRatio")
        val GRADIENT_DRAWABLE_STATE_FIELD_CENTER_X = Key<Float>(Build.VERSION_CODES.N, "mCenterX")
        val GRADIENT_DRAWABLE_STATE_FIELD_CENTER_Y = Key<Float>(Build.VERSION_CODES.N, "mCenterY")
        val GRADIENT_DRAWABLE_STATE_FIELD_CORNER_RADIUS = Key<Float>(Build.VERSION_CODES.N, "mRadius")
        val GRADIENT_DRAWABLE_STATE_FIELD_CORNER_RADII = Key<FloatArray?>(Build.VERSION_CODES.N, "mRadiusArray")
        val GRADIENT_DRAWABLE_STATE_FIELD_SHAPE = Key<Int>(Build.VERSION_CODES.N, "mShape")
        val GRADIENT_DRAWABLE_STATE_FIELD_USE_LEVEL = Key<Boolean>(Build.VERSION_CODES.N, "mUseLevel")
        val GRADIENT_DRAWABLE_STATE_FIELD_COLOR = Key<ColorStateList?>(Build.VERSION_CODES.N, "mColorStateList")
        val GRADIENT_DRAWABLE_STATE_FIELD_COLORS_API_21 = Key<IntArray?>(Build.VERSION_CODES.N, "mColors")
        val GRADIENT_DRAWABLE_STATE_FIELD_COLORS_API_23 = Key<IntArray?>(Build.VERSION_CODES.N, "mGradientColors")

        fun <R> Drawable.ConstantState.field(key: Key<R>): Property<R> {
            return GRADIENT_DRAWABLE_STATE_API
                ?.field(key)
                ?.let {
                    Property.Known(it.get(this))
                }
                ?: Property.Unknown(key.api)
        }
    }
}
