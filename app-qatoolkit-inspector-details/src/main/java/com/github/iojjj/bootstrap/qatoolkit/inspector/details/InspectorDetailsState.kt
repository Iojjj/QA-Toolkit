package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.CategorizedAttributes

sealed interface InspectorDetailsState {

    object Loading : InspectorDetailsState
    data class Loaded(
        val categorizedAttributes: List<CategorizedAttributes>,
        val attributesStatus: AttributesStatus,
    ) : InspectorDetailsState
}