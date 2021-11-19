package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.github.iojjj.bootstrap.qatoolkit.BridgeInfo

data class ViewNode(
    val id: String?,
    override val bounds: Rect,
    override val children: List<ViewNode>,
    val bridgeInfo: BridgeInfo,
) : Node {

    fun findAllNodesUnder(offset: Offset): List<ViewNode> {
        return if (!bounds.contains(offset)) {
            emptyList()
        } else {
            children.flatMap { it.findAllNodesUnder(offset) } + this
        }
    }
}