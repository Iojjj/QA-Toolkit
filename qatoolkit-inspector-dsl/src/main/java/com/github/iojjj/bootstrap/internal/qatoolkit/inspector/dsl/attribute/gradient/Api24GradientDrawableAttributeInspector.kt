package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

import android.graphics.PointF
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Corners
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Shape

@RequiresApi(Build.VERSION_CODES.N)
internal open class Api24GradientDrawableAttributeInspector : Api23GradientDrawableAttributeInspector() {

    override fun inspectType(drawable: GradientDrawable): Property<GradientType> {
        return Property.Known(
            when (val type = drawable.gradientType) {
                GradientDrawable.LINEAR_GRADIENT -> GradientType.Linear
                GradientDrawable.RADIAL_GRADIENT -> GradientType.Radial(drawable.gradientRadius)
                GradientDrawable.SWEEP_GRADIENT -> GradientType.Sweep
                else -> GradientType.Unsupported(type)
            }
        )
    }

    override fun inspectCenter(drawable: GradientDrawable): Property<PointF> {
        return Property.Known(
            PointF(drawable.gradientCenterX, drawable.gradientCenterY)
        )
    }

    override fun getCorners(drawable: GradientDrawable): Property<Corners> {
        return Property.Known(
            when (val cornerRadii = drawable.cornerRadiiSafe) {
                null -> {
                    drawable.cornerRadius.takeUnless { it == 0.0f }
                        ?.let { Corners.Radius(it) }
                        ?: Corners.None
                }
                else -> {
                    Corners.Custom(
                        topLeft = cornerRadii[0],
                        topRight = cornerRadii[2],
                        bottomRight = cornerRadii[4],
                        bottomLeft = cornerRadii[6],
                    )
                }
            }
        )
    }

    override fun inspectColors(drawable: GradientDrawable): Property<GradientColors> {
        return Property.Known(
            when (val colors = drawable.colors) {
                null -> {
                    drawable.color
                        ?.let { GradientColors.Specified(intArrayOf(it.defaultColor)) }
                        ?: GradientColors.None
                }
                else -> {
                    GradientColors.Specified(colors)
                }
            }
        )
    }

    override fun inspectShape(drawable: GradientDrawable): Property<Shape> {
        return Property.Known(
            when (val shape = drawable.shape) {
                GradientDrawable.LINE -> Shape.Line
                GradientDrawable.RECTANGLE -> getRectangleShape(drawable)
                GradientDrawable.OVAL -> Shape.Oval
                GradientDrawable.RING -> getRingShape(drawable)
                else -> Shape.Unsupported(shape)
            }
        )
    }

    override fun inspectUseLevel(drawable: GradientDrawable): Property<Boolean> {
        return Property.Known(
            drawable.useLevel
        )
    }

    companion object {

        val GradientDrawable.cornerRadiiSafe: FloatArray?
            get() {
                return try {
                    cornerRadii
                } catch (e: NullPointerException) {
                    null
                }
            }
    }
}