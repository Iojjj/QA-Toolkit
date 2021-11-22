package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

internal sealed interface Corners {
    object None : Corners

    data class Radius(
        val value: Float
    ) : Corners

    data class Custom(
        val topLeft: Float,
        val topRight: Float,
        val bottomRight: Float,
        val bottomLeft: Float,
    ) : Corners
}