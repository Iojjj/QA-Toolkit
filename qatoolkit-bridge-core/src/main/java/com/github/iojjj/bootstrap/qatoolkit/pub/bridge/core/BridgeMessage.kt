package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core

import android.os.Parcelable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.CategorizedAttributes
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.ServiceRequest
import kotlinx.parcelize.Parcelize

sealed class BridgeMessage {

    @Parcelize
    data class LoadAttributesRequest(
        val uuid: String
    ) : BridgeMessage(),
        ServiceRequest<LoadAttributesRequest.Response> {

        sealed class Response : Parcelable {

            @Parcelize
            data class ViewFound(
                val uuid: String,
                val categories: List<CategorizedAttributes>
            ) : Response()

            @Parcelize
            object ViewNotFound : Response()
        }
    }
}
