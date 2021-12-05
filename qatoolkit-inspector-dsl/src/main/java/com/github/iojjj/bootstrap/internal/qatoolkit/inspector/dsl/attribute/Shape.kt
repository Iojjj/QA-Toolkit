package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

internal sealed interface Shape {
    object Line : Shape

    class RectangleWithoutKnownAttributes(
        val minApi: Int
    ) : Shape

    class Rectangle(
        val corners: Corners
    ) : Shape

    object Oval : Shape

    data class RingWithoutKnownAttributes(
        val minApi: Int
    ) : Shape

    data class Ring(
        val innerRadius: Int,
        val innerRadiusRatio: Float,
        val thickness: Int,
        val thicknessRatio: Float,
    ) : Shape

    data class Unsupported(
        val value: Int
    ) : Shape
}