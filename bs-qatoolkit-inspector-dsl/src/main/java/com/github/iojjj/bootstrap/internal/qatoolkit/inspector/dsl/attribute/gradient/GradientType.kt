package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

internal sealed interface GradientType {
    object Linear : GradientType

    data class Radial(
        val radius: Float
    ) : GradientType

    object Sweep : GradientType

    data class Unsupported(
        val value: Int
    ) : GradientType
}