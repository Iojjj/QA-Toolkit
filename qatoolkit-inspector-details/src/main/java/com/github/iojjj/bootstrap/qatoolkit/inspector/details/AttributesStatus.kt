package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
sealed interface AttributesStatus {

    @get:DrawableRes
    val icon: Int

    @get:StringRes
    val message: Int

    object Ok : AttributesStatus {
        override val icon: Int = R.drawable.qatoolkit_inspector_details_ic_status_info_24dp
        override val message: Int = R.string.qatoolkit_inspector_details_attributes_status_ok
    }

    object BridgeNotConfigured : AttributesStatus {
        override val icon: Int = R.drawable.qatoolkit_inspector_details_ic_status_warning_24dp
        override val message: Int = R.string.qatoolkit_inspector_details_attributes_status_bridge_not_configured
    }

    object ConnectionIssue : AttributesStatus {
        override val icon: Int = R.drawable.qatoolkit_inspector_details_ic_status_warning_24dp
        override val message: Int = R.string.qatoolkit_inspector_details_attributes_status_connection_issue
    }

    object InternalError : AttributesStatus {
        override val icon: Int = R.drawable.qatoolkit_inspector_details_ic_status_warning_24dp
        override val message: Int = R.string.qatoolkit_inspector_details_attributes_status_internal_error
    }

    object ViewNotFound : AttributesStatus {
        override val icon: Int = R.drawable.qatoolkit_inspector_details_ic_status_warning_24dp
        override val message: Int = R.string.qatoolkit_inspector_details_attributes_status_view_not_found
    }

    object VirtualView : AttributesStatus {
        override val icon: Int = R.drawable.qatoolkit_inspector_details_ic_status_warning_24dp
        override val message: Int = R.string.qatoolkit_inspector_details_attributes_status_virtual_view
    }
}
