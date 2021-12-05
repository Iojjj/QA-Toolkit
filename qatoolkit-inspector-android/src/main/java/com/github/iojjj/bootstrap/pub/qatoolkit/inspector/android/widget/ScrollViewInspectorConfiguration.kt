package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.ScrollView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [ScrollView] type.
 */
class ScrollViewInspectorConfiguration : InspectorConfiguration<ScrollView> {

    override fun configure(): Iterable<CategoryInspector<ScrollView>> = Inspect {
        VerticalScrollCategory {
            Dimension(ATTRIBUTE_MAX_SCROLL_AMOUNT) { it.maxScrollAmount }
            Boolean(ATTRIBUTE_FILL_VIEWPORT) { it.isFillViewport }
            Boolean(ATTRIBUTE_SMOOTH_SCROLL_ENABLED) { it.isSmoothScrollingEnabled }
        }
        VerticalScrollEdgeEffectCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Color(ATTRIBUTE_TOP_EDGE_EFFECT_COLOR) { it.topEdgeEffectColor }
                Color(ATTRIBUTE_BOTTOM_EDGE_EFFECT_COLOR) { it.bottomEdgeEffectColor }
            } else {
                ApiRestricted(ATTRIBUTE_TOP_EDGE_EFFECT_COLOR, Build.VERSION_CODES.Q)
                ApiRestricted(ATTRIBUTE_BOTTOM_EDGE_EFFECT_COLOR, Build.VERSION_CODES.Q)
            }
        }
    }

    companion object {

        const val CATEGORY_VERTICAL_SCROLL: String = "Vertical Scroll"
        const val CATEGORY_VERTICAL_SCROLL_EDGE_EFFECT: String = "Vertical Scroll: Edge Effect"

        const val CATEGORY_ORDER_VERTICAL_SCROLL_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_VERTICAL_SCROLL: Int = CATEGORY_ORDER_VERTICAL_SCROLL_FIRST
        const val CATEGORY_ORDER_VERTICAL_SCROLL_EDGE_EFFECT: Int = CATEGORY_ORDER_VERTICAL_SCROLL + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_VERTICAL_SCROLL_LAST: Int = CATEGORY_ORDER_VERTICAL_SCROLL_EDGE_EFFECT

        const val ATTRIBUTE_FILL_VIEWPORT: String = "Is content fills viewport"
        const val ATTRIBUTE_SMOOTH_SCROLL_ENABLED: String = "Is smooth scroll enabled"
        const val ATTRIBUTE_MAX_SCROLL_AMOUNT: String = "Max scroll amount"
        const val ATTRIBUTE_TOP_EDGE_EFFECT_COLOR: String = "Top edge effect color"
        const val ATTRIBUTE_BOTTOM_EDGE_EFFECT_COLOR: String = "Bottom edge effect color"

        fun <T> InspectorBuilder<T>.VerticalScrollCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_VERTICAL_SCROLL, CATEGORY_ORDER_VERTICAL_SCROLL, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.VerticalScrollEdgeEffectCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_VERTICAL_SCROLL_EDGE_EFFECT, CATEGORY_ORDER_VERTICAL_SCROLL_EDGE_EFFECT, allowNulls, block)
        }
    }
}