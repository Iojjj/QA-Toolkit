package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.webkit

import android.webkit.WebView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Bitmap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [WebView] type.
 */
class WebViewInspectorConfiguration : InspectorConfiguration<WebView> {

    override fun configure(): Iterable<CategoryInspector<WebView>> = Inspect {
        WebViewPageCategory {
            String(ATTRIBUTE_URL) { it.url }
            String(ATTRIBUTE_TITLE) { it.title }
            Dimension(ATTRIBUTE_CONTENT_HEIGHT) { it.contentHeight }
            Bitmap(ATTRIBUTE_FAVICON) { it.favicon }
            Boolean(ATTRIBUTE_CAN_GO_BACK) { it.canGoBack() }
            Boolean(ATTRIBUTE_CAN_GO_FORWARD) { it.canGoForward() }
        }
        WebViewOtherCategory {
            Boolean(ATTRIBUTE_PRIVATE_BROWSING_ENABLED) { it.isPrivateBrowsingEnabled }
        }
    }

    companion object {

        const val CATEGORY_WEB_VIEW_PAGE = "WebView: Page"
        const val CATEGORY_WEB_VIEW_OTHER = "WebView: Other"

        const val CATEGORY_ORDER_WEB_VIEW_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_WEB_VIEW_PAGE: Int = CATEGORY_ORDER_WEB_VIEW_FIRST
        const val CATEGORY_ORDER_WEB_VIEW_OTHER: Int = CATEGORY_ORDER_WEB_VIEW_PAGE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_WEB_VIEW_LAST: Int = CATEGORY_ORDER_WEB_VIEW_OTHER

        const val ATTRIBUTE_CONTENT_HEIGHT: String = "Content height"
        const val ATTRIBUTE_FAVICON: String = "Favicon"
        const val ATTRIBUTE_PRIVATE_BROWSING_ENABLED: String = "Is private browsing enabled"
        const val ATTRIBUTE_TITLE: String = "Title"
        const val ATTRIBUTE_URL: String = "Url"
        const val ATTRIBUTE_CAN_GO_BACK: String = "Can go back"
        const val ATTRIBUTE_CAN_GO_FORWARD: String = "Can go forward"

        fun <T> InspectorBuilder<T>.WebViewPageCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_PAGE, CATEGORY_ORDER_WEB_VIEW_PAGE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_OTHER, CATEGORY_ORDER_WEB_VIEW_OTHER, allowNulls, block)
        }
    }
}