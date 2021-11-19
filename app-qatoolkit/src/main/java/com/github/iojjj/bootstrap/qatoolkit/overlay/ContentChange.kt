package com.github.iojjj.bootstrap.qatoolkit.overlay

sealed interface ContentChange {

    sealed interface VisibilityChange: ContentChange

    object ContentNotVisible : VisibilityChange

    data class ContentVisible(
        val width: Int,
        val height: Int,
        val isLayoutInScreen: Boolean,
        val isFocusable: Boolean,
        val isTouchable: Boolean,
        val isNotTouchModal: Boolean,
        val isImeInputRequired: Boolean,
    ) : VisibilityChange

    data class ChangeSize(
        val width: Int,
        val height: Int,
    ) : ContentChange

    data class UpdatePosition(
        val x: Int,
        val y: Int,
    ) : ContentChange
}