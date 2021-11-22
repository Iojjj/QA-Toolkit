package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.compose.ui.geometry.Rect

interface Node {
    val bounds: Rect
    val children: List<Node>
}