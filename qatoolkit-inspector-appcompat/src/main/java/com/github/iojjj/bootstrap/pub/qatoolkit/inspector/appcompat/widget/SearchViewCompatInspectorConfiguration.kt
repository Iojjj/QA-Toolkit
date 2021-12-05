package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.appcompat.widget

import androidx.appcompat.widget.SearchView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_ICONIFIED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_ICONIFIED_BY_DEFAULT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_MAX_WIDTH
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_QUERY
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_QUERY_HINT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_QUERY_REFINEMENT_ENABLED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.ATTRIBUTE_SUBMIT_BUTTON_ENABLED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.SearchViewOtherCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SearchViewInspectorConfiguration.Companion.SearchViewQueryCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String

/**
 * Implementation of [InspectorConfiguration] for [SearchView] type.
 */
class SearchViewCompatInspectorConfiguration : InspectorConfiguration<SearchView> {

    override fun configure(): Iterable<CategoryInspector<SearchView>> = Inspect {
        SearchViewQueryCategory {
            String(ATTRIBUTE_QUERY) { it.query }
            String(ATTRIBUTE_QUERY_HINT) { it.queryHint }
            Boolean(ATTRIBUTE_QUERY_REFINEMENT_ENABLED) { it.isQueryRefinementEnabled }
        }
        SearchViewOtherCategory {
            Dimension(ATTRIBUTE_MAX_WIDTH) { it.maxWidth }
            Boolean(ATTRIBUTE_ICONIFIED) { it.isIconified }
            Boolean(ATTRIBUTE_ICONIFIED_BY_DEFAULT) { it.isIconfiedByDefault }
            Boolean(ATTRIBUTE_SUBMIT_BUTTON_ENABLED) { it.isSubmitButtonEnabled }
        }
    }
}