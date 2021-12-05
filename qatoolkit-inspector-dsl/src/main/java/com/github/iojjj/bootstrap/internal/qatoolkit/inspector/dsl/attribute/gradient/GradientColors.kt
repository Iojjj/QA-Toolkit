package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

internal sealed interface GradientColors {
    object None : GradientColors

    class Specified(
        val values: IntArray
    ) : GradientColors
}