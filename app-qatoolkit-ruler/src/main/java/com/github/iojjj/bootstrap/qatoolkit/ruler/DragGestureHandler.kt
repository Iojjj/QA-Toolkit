package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.runtime.Stable
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope

@Stable
internal interface DragGestureHandler {

    @Stable
    val onDragStart: PointerInputScope.() -> Unit

    @Stable
    val onDragEnd: PointerInputScope.() -> Unit

    @Stable
    val onDrag: PointerInputScope.(PointerInputChange, Float) -> Unit
}