package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

import android.graphics.drawable.GradientDrawable
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientColors
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientType
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute.Companion.color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.apiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.apiRestrictedStringDecapitalize
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.float
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.fold
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.foldSequence

internal fun gradientDrawableAttributes(
    name: String,
    drawable: GradientDrawable,
): Sequence<Attribute> {
    return sequenceOf(
        gradientType(name, drawable),
        gradientCenter(name, drawable),
        gradientShape(name, drawable),
        gradientColors(name, drawable),
        gradientUseLevel(name, drawable),
    ).flatten()
}

private fun gradientType(
    name: String,
    drawable: GradientDrawable
): Sequence<Attribute> {
    return sequenceOf(
        GradientDrawableAttributeInspector.inspectType(drawable).fold(
            attribute = "$name: Type",
            onKnown = { attribute, type ->
                val gradientType = when (type) {
                    GradientType.Linear -> "Linear gradient"
                    is GradientType.Radial -> "Radial gradient"
                    GradientType.Sweep -> "Sweep gradient"
                    is GradientType.Unsupported -> "Gradient (unsupported type: ${type.value})"
                }
                string(attribute, gradientType)
            },
            onUnknown = { attribute, minApi ->
                string(attribute, "Gradient (${apiRestrictedStringDecapitalize(minApi)}")
            }
        ),
    )
}

private fun gradientCenter(
    name: String,
    drawable: GradientDrawable
): Sequence<Attribute> {
    return GradientDrawableAttributeInspector.inspectCenter(drawable).foldSequence(
        name = "$name: Center",
        onKnown = { attribute, value ->
            sequenceOf(
                float("$attribute X", value.x),
                float("$attribute Y", value.y),
            )
        },
        onUnknown = { attribute, api ->
            sequenceOf(
                apiRestricted("$attribute X", api),
                apiRestricted("$attribute Y", api),
            )
        }
    )
}

private fun gradientShape(
    name: String,
    drawable: GradientDrawable
): Sequence<Attribute> {
    return GradientDrawableAttributeInspector.inspectShape(drawable).fold(
        onKnown = { shape ->
            val attributeShape = "$name: Shape"
            val attributeRectangleCorners = "$name: Rectangle Corners"
            val attributeRingInnerRadius = "$name: Ring Inner Radius"
            val attributeRingThickness = "$name: Ring Thickness"
            when (shape) {
                Shape.Line -> {
                    sequenceOf(string(attributeShape, "Line"))
                }
                Shape.Oval -> {
                    sequenceOf(string(attributeShape, "Oval"))
                }
                is Shape.Rectangle -> {
                    when (shape.corners) {
                        Corners.None -> {
                            sequenceOf(
                                string(attributeShape, "Rectangle"),
                            )
                        }
                        is Corners.Radius -> {
                            sequenceOf(
                                string(attributeShape, "Rectangle"),
                                dimension(attributeRectangleCorners, shape.corners.value),
                            )
                        }
                        is Corners.Custom -> {
                            sequenceOf(
                                string(attributeShape, "Rectangle"),
                                dimension("$attributeRectangleCorners (Top Left)", shape.corners.topLeft),
                                dimension("$attributeRectangleCorners (Top Right)", shape.corners.topRight),
                                dimension("$attributeRectangleCorners (Bottom Right)", shape.corners.bottomRight),
                                dimension("$attributeRectangleCorners (Bottom Left)", shape.corners.bottomLeft),
                            )
                        }
                    }
                }
                is Shape.RectangleWithoutKnownAttributes -> {
                    sequenceOf(
                        string(attributeShape, "Rectangle"),
                        apiRestricted(attributeRectangleCorners, shape.minApi),
                    )
                }
                is Shape.Ring -> {
                    sequenceOf(
                        string(attributeShape, "Ring"),
                        shape.innerRadius.takeUnless { it < 0 }
                            ?.let { dimension(attributeRingInnerRadius, it) }
                            ?: float("$attributeRingInnerRadius Ratio", shape.innerRadiusRatio),
                        shape.thickness.takeUnless { it < 0 }
                            ?.let { dimension(attributeRingThickness, it) }
                            ?: float("$attributeRingThickness Ratio", shape.thicknessRatio),
                    ).filterNotNull()
                }
                is Shape.RingWithoutKnownAttributes -> {
                    sequenceOf(
                        string(attributeShape, "Ring"),
                        apiRestricted(attributeRingInnerRadius, shape.minApi),
                        apiRestricted(attributeRingThickness, shape.minApi),
                    )
                }
                is Shape.Unsupported -> {
                    sequenceOf(
                        string(attributeShape, "Unsupported (${shape.value})"),
                    )
                }
            }
        },
        onUnknown = {
            sequenceOf(apiRestricted("$name: Shape", it))
        },
    )
}

private fun gradientColors(
    name: String,
    drawable: GradientDrawable
): Sequence<Attribute> {
    return GradientDrawableAttributeInspector.inspectColors(drawable).foldSequence(
        name = "$name: Colors",
        onKnown = { attribute, colors ->
            when (colors) {
                GradientColors.None -> {
                    sequenceOf(string(attribute, "No colors specified"))
                }
                is GradientColors.Specified -> {
                    when (colors.values.size) {
                        1 -> {
                            sequenceOf(color("$name: Single Color", colors.values[0]))
                        }
                        2 -> {
                            sequenceOf(
                                color("${name}: Start Color", colors.values[0]),
                                color("${name}: End Color", colors.values[1])
                            )
                        }
                        3 -> {
                            sequenceOf(
                                color("${name}: Start Color", colors.values[0]),
                                color("${name}: Center Color", colors.values[1]),
                                color("${name}: End Color", colors.values[2])
                            )
                        }
                        else -> {
                            colors.values.asSequence()
                                .mapIndexed { index, color -> color("$name: Color #${index + 1}", color) }
                        }
                    }
                }
            }
        },
        onUnknown = { attribute, api ->
            sequenceOf(apiRestricted(attribute, api))
        }
    )
}

private fun gradientUseLevel(
    name: String,
    drawable: GradientDrawable
): Sequence<Attribute> {
    return sequenceOf(
        GradientDrawableAttributeInspector.inspectUseLevel(drawable)
            .fold("$name: Use Level") { attribute, value ->
                boolean(attribute, value)
            }
    )
}
