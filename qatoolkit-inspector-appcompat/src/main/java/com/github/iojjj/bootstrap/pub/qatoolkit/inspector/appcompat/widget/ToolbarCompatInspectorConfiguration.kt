package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.appcompat.widget

import androidx.appcompat.widget.Toolbar
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_CONTENT_INSET_END
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_CONTENT_INSET_END_WITH_ACTIONS
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_CONTENT_INSET_START
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_CONTENT_INSET_START_WITH_NAVIGATION
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_CURRENT_CONTENT_INSET_END
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_CURRENT_CONTENT_INSET_START
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_SUBTITLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_TITLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_TITLE_MARGIN_BOTTOM
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_TITLE_MARGIN_END
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_TITLE_MARGIN_START
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ATTRIBUTE_TITLE_MARGIN_TOP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ToolbarContentInsetsCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ToolbarSubtitleCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ToolbarInspectorConfiguration.Companion.ToolbarTitleCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.FlatMap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String

/**
 * Implementation of [InspectorConfiguration] for [Toolbar] type.
 */
class ToolbarCompatInspectorConfiguration : InspectorConfiguration<Toolbar> {

    override fun configure(): Iterable<CategoryInspector<Toolbar>> = Inspect {
        ToolbarTitleCategory {
            FlatMap(ATTRIBUTE_TITLE) {
                val title = it.title
                if (title.isNullOrEmpty()) {
                    emptyList()
                } else {
                    listOf(
                        string(ATTRIBUTE_TITLE, title),
                        dimension(ATTRIBUTE_TITLE_MARGIN_START, it.titleMarginStart),
                        dimension(ATTRIBUTE_TITLE_MARGIN_TOP, it.titleMarginTop),
                        dimension(ATTRIBUTE_TITLE_MARGIN_END, it.titleMarginEnd),
                        dimension(ATTRIBUTE_TITLE_MARGIN_BOTTOM, it.titleMarginBottom),
                    )
                }
            }
        }
        ToolbarSubtitleCategory {
            String(ATTRIBUTE_SUBTITLE) { it.subtitle }
        }
        ToolbarContentInsetsCategory {
            Dimension(ATTRIBUTE_CONTENT_INSET_START) { it.contentInsetStart }
            Dimension(ATTRIBUTE_CONTENT_INSET_END) { it.contentInsetEnd }
            Dimension(ATTRIBUTE_CONTENT_INSET_START_WITH_NAVIGATION) { it.contentInsetStartWithNavigation }
            Dimension(ATTRIBUTE_CONTENT_INSET_END_WITH_ACTIONS) { it.contentInsetEndWithActions }
            Dimension(ATTRIBUTE_CURRENT_CONTENT_INSET_START) { it.currentContentInsetStart }
            Dimension(ATTRIBUTE_CURRENT_CONTENT_INSET_END) { it.currentContentInsetEnd }
        }
    }
}