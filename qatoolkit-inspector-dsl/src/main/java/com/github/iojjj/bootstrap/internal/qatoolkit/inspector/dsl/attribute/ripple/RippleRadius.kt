package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.ripple

internal sealed interface RippleRadius {
    object Auto : RippleRadius
    data class Fixed(val value: Float) : RippleRadius
}