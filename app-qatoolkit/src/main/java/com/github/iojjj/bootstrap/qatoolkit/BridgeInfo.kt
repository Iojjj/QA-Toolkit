package com.github.iojjj.bootstrap.qatoolkit

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.CategorizedAttributes

sealed interface BridgeInfo {

    val categories: List<CategorizedAttributes>

    data class NotAvailable(
        override val categories: List<CategorizedAttributes>
    ) : BridgeInfo

    data class Available(
        val uuid: String,
        val servicePackageName: String,
        override val categories: List<CategorizedAttributes>,
    ) : BridgeInfo

    data class ViewNotConfigured(
        override val categories: List<CategorizedAttributes>
    ) : BridgeInfo
}