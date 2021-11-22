package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect

interface Transformable {

    fun toDrawOffset(offset: Offset): Offset

    fun toDrawOffset(offset: IntOffset): IntOffset

    fun toDrawRect(rect: Rect): Rect

    fun toDrawRect(rect: IntRect): IntRect

    fun toDrawRect(roundRect: RoundRect): RoundRect

    fun fromDrawOffset(offset: Offset): Offset
}