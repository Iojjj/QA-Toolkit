package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

import android.graphics.PointF
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Corners
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Shape
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_CENTER_X
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_CENTER_Y
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_COLOR
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_COLORS_API_21
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_CORNER_RADII
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_CORNER_RADIUS
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_GRADIENT
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_INNER_RADIUS
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_INNER_RADIUS_RATIO
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_SHAPE
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_THICKNESS
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_THICKNESS_RATIO
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_USE_LEVEL
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.field
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection.ReflectionField

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal open class Api21GradientDrawableAttributeInspector : GradientDrawableAttributeInspector {

    override fun inspectType(drawable: GradientDrawable): Property<GradientType> {
        val drawableState = drawable.constantState!!
        return when (val gradientTypeProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_GRADIENT)) {
            is Property.Known -> {
                Property.Known(
                    when (val type = gradientTypeProperty.value) {
                        GradientDrawable.LINEAR_GRADIENT -> GradientType.Linear
                        GradientDrawable.RADIAL_GRADIENT -> GradientType.Radial(drawable.gradientRadius)
                        GradientDrawable.SWEEP_GRADIENT -> GradientType.Sweep
                        else -> GradientType.Unsupported(type)
                    }
                )
            }
            is Property.Unknown -> {
                Property.Unknown(gradientTypeProperty.api)
            }
        }
    }

    override fun inspectCenter(drawable: GradientDrawable): Property<PointF> {
        val drawableState = drawable.constantState!!
        val cX = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_CENTER_X)
        val cY = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_CENTER_Y)
        return if (cX is Property.Known && cY is Property.Known) {
            Property.Known(PointF(cX.value, cY.value))
        } else {
            Property.Unknown(GRADIENT_DRAWABLE_STATE_FIELD_CENTER_X.api)
        }
    }

    override fun getCorners(drawable: GradientDrawable): Property<Corners> {
        val drawableState = drawable.constantState!!
        return when (val cornerRadiiProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_CORNER_RADII)) {
            is Property.Unknown -> {
                Property.Unknown(cornerRadiiProperty.api)
            }
            is Property.Known -> {
                when (val cornerRadii = cornerRadiiProperty.value) {
                    null -> {
                        when (val cornerRadiusProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_CORNER_RADIUS)) {
                            is Property.Known -> {
                                Property.Known(
                                    cornerRadiusProperty.value.takeUnless { it == 0.0f }
                                        ?.let { Corners.Radius(it) }
                                        ?: Corners.None
                                )
                            }
                            is Property.Unknown -> {
                                Property.Unknown(cornerRadiusProperty.api)
                            }
                        }
                    }
                    else -> {
                        Property.Known(
                            Corners.Custom(
                                topLeft = cornerRadii[0],
                                topRight = cornerRadii[2],
                                bottomRight = cornerRadii[4],
                                bottomLeft = cornerRadii[6],
                            )
                        )
                    }
                }
            }
        }
    }

    override fun inspectColors(drawable: GradientDrawable): Property<GradientColors> {
        return inspectColors(drawable, GRADIENT_DRAWABLE_STATE_FIELD_COLORS_API_21)
    }

    override fun inspectShape(drawable: GradientDrawable): Property<Shape> {
        val drawableState = drawable.constantState!!
        return when (val shapeProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_SHAPE)) {
            is Property.Known -> {
                Property.Known(
                    when (val shape = shapeProperty.value) {
                        GradientDrawable.LINE -> Shape.Line
                        GradientDrawable.RECTANGLE -> getRectangleShape(drawable)
                        GradientDrawable.OVAL -> Shape.Oval
                        GradientDrawable.RING -> getRingShape(drawable)
                        else -> Shape.Unsupported(shape)
                    }
                )
            }
            is Property.Unknown -> {
                Property.Unknown(shapeProperty.api)
            }
        }
    }

    override fun getRectangleShape(drawable: GradientDrawable): Shape {
        return when (val corners = getCorners(drawable)) {
            is Property.Known -> {
                Shape.Rectangle(corners.value)
            }
            else -> {
                Shape.RectangleWithoutKnownAttributes(Build.VERSION_CODES.N)
            }
        }
    }

    override fun getRingShape(drawable: GradientDrawable): Shape {
        val drawableState = drawable.constantState!!
        val innerRadiusProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_INNER_RADIUS)
        val innerRadiusRatioProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_INNER_RADIUS_RATIO)
        val thicknessProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_THICKNESS)
        val thicknessRatioProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_THICKNESS_RATIO)
        return if (innerRadiusProperty is Property.Known &&
            innerRadiusRatioProperty is Property.Known &&
            thicknessProperty is Property.Known &&
            thicknessRatioProperty is Property.Known
        ) {
            Shape.Ring(
                innerRadius = innerRadiusProperty.value,
                innerRadiusRatio = innerRadiusRatioProperty.value,
                thickness = thicknessProperty.value,
                thicknessRatio = thicknessRatioProperty.value,
            )
        } else {
            Shape.RingWithoutKnownAttributes(Build.VERSION_CODES.Q)
        }
    }

    override fun inspectUseLevel(drawable: GradientDrawable): Property<Boolean> {
        val drawableState = drawable.constantState!!
        return when (val useLevelProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_USE_LEVEL)) {
            is Property.Known -> {
                Property.Known(useLevelProperty.value)
            }
            is Property.Unknown -> {
                Property.Unknown(useLevelProperty.api)
            }
        }
    }

    protected fun inspectColors(
        drawable: GradientDrawable,
        fieldKey: ReflectionField.Key<IntArray?>
    ): Property<GradientColors> {
        val drawableState = drawable.constantState!!
        return when (val colorsProperty = drawableState.field(fieldKey)) {
            is Property.Known -> {
                when (val colors = colorsProperty.value) {
                    null -> {
                        when (val colorProperty = drawableState.field(GRADIENT_DRAWABLE_STATE_FIELD_COLOR)) {
                            is Property.Known -> {
                                Property.Known(
                                    colorProperty.value
                                        ?.let { GradientColors.Specified(intArrayOf(it.defaultColor)) }
                                        ?: GradientColors.None
                                )
                            }
                            is Property.Unknown -> {
                                Property.Unknown(colorProperty.api)
                            }
                        }
                    }
                    else -> {
                        Property.Known(
                            GradientColors.Specified(colors)
                        )
                    }
                }
            }
            is Property.Unknown -> {
                Property.Unknown(colorsProperty.api)
            }
        }
    }
}
