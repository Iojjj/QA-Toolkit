package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

data class SimpleIconText(
    override val icon: Int,
    override val text: String,
    override val isEnabled: Boolean = true,
) : ToolkitBarMenuItem.IconText