package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.SearchView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [SearchView] type.
 */
class SearchViewInspectorConfiguration : InspectorConfiguration<SearchView> {

    override fun configure(): Iterable<CategoryInspector<SearchView>> = Inspect {
        SearchViewQueryCategory {
            String(ATTRIBUTE_QUERY) { it.query }
            String(ATTRIBUTE_QUERY_HINT) { it.queryHint }
            Boolean(ATTRIBUTE_QUERY_REFINEMENT_ENABLED) { it.isQueryRefinementEnabled }
        }
        SearchViewOtherCategory {
            Dimension(ATTRIBUTE_MAX_WIDTH) { it.maxWidth }
            Boolean(ATTRIBUTE_ICONIFIED) { it.isIconified }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Boolean(ATTRIBUTE_ICONIFIED_BY_DEFAULT) { it.isIconifiedByDefault }
            } else {
                ApiRestricted(ATTRIBUTE_ICONIFIED_BY_DEFAULT, Build.VERSION_CODES.Q)
            }
            Boolean(ATTRIBUTE_SUBMIT_BUTTON_ENABLED) { it.isSubmitButtonEnabled }
        }
    }

    companion object {

        const val CATEGORY_SEARCH_VIEW_QUERY: String = "Search View: Query"
        const val CATEGORY_SEARCH_VIEW_OTHER: String = "Search View: Other"

        const val CATEGORY_ORDER_SEARCH_VIEW_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_SEARCH_VIEW_QUERY: Int = CATEGORY_ORDER_SEARCH_VIEW_FIRST
        const val CATEGORY_ORDER_SEARCH_VIEW_OTHER: Int = CATEGORY_ORDER_SEARCH_VIEW_QUERY + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_SEARCH_VIEW_LAST: Int = CATEGORY_ORDER_SEARCH_VIEW_OTHER

        const val ATTRIBUTE_QUERY: String = "Query"
        const val ATTRIBUTE_QUERY_HINT: String = "Query hint"
        const val ATTRIBUTE_QUERY_REFINEMENT_ENABLED: String = "Is query refinement enabled"
        const val ATTRIBUTE_ICONIFIED: String = "Is iconified"
        const val ATTRIBUTE_ICONIFIED_BY_DEFAULT: String = "Is iconified by default"
        const val ATTRIBUTE_SUBMIT_BUTTON_ENABLED: String = "Is submit button enabled"
        const val ATTRIBUTE_MAX_WIDTH: String = "Max width"

        fun <T> InspectorBuilder<T>.SearchViewQueryCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_SEARCH_VIEW_QUERY, CATEGORY_ORDER_SEARCH_VIEW_QUERY, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.SearchViewOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_SEARCH_VIEW_OTHER, CATEGORY_ORDER_SEARCH_VIEW_OTHER, allowNulls, block)
        }
    }
}