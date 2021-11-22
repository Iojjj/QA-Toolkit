package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.Toolbar
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.apiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.FlatMap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [Toolbar] type.
 */
class ToolbarInspectorConfiguration : InspectorConfiguration<Toolbar> {

    override fun configure(): Iterable<CategoryInspector<Toolbar>> = Inspect {
        ToolbarTitleCategory {
            FlatMap(ATTRIBUTE_TITLE) {
                val title = it.title
                if (title.isNullOrEmpty()) {
                    emptyList()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        listOf(
                            string(ATTRIBUTE_TITLE, title),
                            dimension(ATTRIBUTE_TITLE_MARGIN_START, it.titleMarginStart),
                            dimension(ATTRIBUTE_TITLE_MARGIN_TOP, it.titleMarginTop),
                            dimension(ATTRIBUTE_TITLE_MARGIN_END, it.titleMarginEnd),
                            dimension(ATTRIBUTE_TITLE_MARGIN_BOTTOM, it.titleMarginBottom),
                        )
                    } else {
                        listOf(
                            string(ATTRIBUTE_TITLE, title),
                            apiRestricted(ATTRIBUTE_TITLE_MARGIN_START, Build.VERSION_CODES.N),
                            apiRestricted(ATTRIBUTE_TITLE_MARGIN_TOP, Build.VERSION_CODES.N),
                            apiRestricted(ATTRIBUTE_TITLE_MARGIN_END, Build.VERSION_CODES.N),
                            apiRestricted(ATTRIBUTE_TITLE_MARGIN_BOTTOM, Build.VERSION_CODES.N),
                        )
                    }
                }
            }
        }
        ToolbarSubtitleCategory {
            String(ATTRIBUTE_SUBTITLE) { it.subtitle }
        }
        ToolbarContentInsetsCategory {
            Dimension(ATTRIBUTE_CONTENT_INSET_START) { it.contentInsetStart }
            Dimension(ATTRIBUTE_CONTENT_INSET_END) { it.contentInsetEnd }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Dimension(ATTRIBUTE_CONTENT_INSET_START_WITH_NAVIGATION) { it.contentInsetStartWithNavigation }
                Dimension(ATTRIBUTE_CONTENT_INSET_END_WITH_ACTIONS) { it.contentInsetEndWithActions }
                Dimension(ATTRIBUTE_CURRENT_CONTENT_INSET_START) { it.currentContentInsetStart }
                Dimension(ATTRIBUTE_CURRENT_CONTENT_INSET_END) { it.currentContentInsetEnd }
            } else {
                ApiRestricted(ATTRIBUTE_CONTENT_INSET_START_WITH_NAVIGATION, Build.VERSION_CODES.N)
                ApiRestricted(ATTRIBUTE_CONTENT_INSET_END_WITH_ACTIONS, Build.VERSION_CODES.N)
                ApiRestricted(ATTRIBUTE_CURRENT_CONTENT_INSET_START, Build.VERSION_CODES.N)
                ApiRestricted(ATTRIBUTE_CURRENT_CONTENT_INSET_END, Build.VERSION_CODES.N)
            }
        }
    }

    companion object {

        const val CATEGORY_TOOLBAR_TITLE: String = "Toolbar: Title"
        const val CATEGORY_TOOLBAR_SUBTITLE: String = "Toolbar: Subtitle"
        const val CATEGORY_TOOLBAR_CONTENT_INSETS: String = "Toolbar: Content Insets"

        const val CATEGORY_ORDER_TOOLBAR_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_TOOLBAR_TITLE: Int = CATEGORY_ORDER_TOOLBAR_FIRST
        const val CATEGORY_ORDER_TOOLBAR_SUBTITLE: Int = CATEGORY_ORDER_TOOLBAR_TITLE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TOOLBAR_CONTENT_INSETS: Int = CATEGORY_ORDER_TOOLBAR_SUBTITLE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TOOLBAR_LAST: Int = CATEGORY_ORDER_TOOLBAR_CONTENT_INSETS

        const val ATTRIBUTE_CONTENT_INSET_START: String = "Content inset start"
        const val ATTRIBUTE_CONTENT_INSET_START_WITH_NAVIGATION: String = "Content inset start with navigation"
        const val ATTRIBUTE_CONTENT_INSET_END: String = "Content inset end"
        const val ATTRIBUTE_CONTENT_INSET_END_WITH_ACTIONS: String = "Content inset end with actions"
        const val ATTRIBUTE_CURRENT_CONTENT_INSET_START: String = "Current content inset start"
        const val ATTRIBUTE_CURRENT_CONTENT_INSET_END: String = "Current content inset end"
        const val ATTRIBUTE_TITLE: String = "Title"
        const val ATTRIBUTE_SUBTITLE: String = "Subtitle"
        const val ATTRIBUTE_TITLE_MARGIN_START: String = "Title margin start"
        const val ATTRIBUTE_TITLE_MARGIN_TOP: String = "Title margin top"
        const val ATTRIBUTE_TITLE_MARGIN_END: String = "Title margin end"
        const val ATTRIBUTE_TITLE_MARGIN_BOTTOM: String = "Title margin bottom"

        fun <T> InspectorBuilder<T>.ToolbarTitleCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TOOLBAR_TITLE, CATEGORY_ORDER_TOOLBAR_TITLE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ToolbarSubtitleCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TOOLBAR_SUBTITLE, CATEGORY_ORDER_TOOLBAR_SUBTITLE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ToolbarContentInsetsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TOOLBAR_CONTENT_INSETS, CATEGORY_ORDER_TOOLBAR_CONTENT_INSETS, allowNulls, block)
        }
    }
}