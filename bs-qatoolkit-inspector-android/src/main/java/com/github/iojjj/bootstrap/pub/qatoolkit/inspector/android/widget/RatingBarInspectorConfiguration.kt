package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.widget.RatingBar
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.AbsSeekBarInspectorConfiguration.Companion.CATEGORY_ORDER_ABS_SEEK_BAR_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Float
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [RatingBar] type.
 */
class RatingBarInspectorConfiguration : InspectorConfiguration<RatingBar> {

    override fun configure(): Iterable<CategoryInspector<RatingBar>> = Inspect {
        RatingBarCategory {
            Boolean(ATTRIBUTE_INDICATOR) { it.isIndicator }
            Int(ATTRIBUTE_NUM_STARS) { it.numStars }
            Float(ATTRIBUTE_RATING) { it.rating }
            Float(ATTRIBUTE_STEP_SIZE) { it.stepSize }
        }
    }

    companion object {

        const val CATEGORY_RATING_BAR: String = "Rating Bar"

        const val CATEGORY_ORDER_RATING_BAR_FIRST: Int = CATEGORY_ORDER_ABS_SEEK_BAR_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_RATING_BAR: Int = CATEGORY_ORDER_RATING_BAR_FIRST
        const val CATEGORY_ORDER_RATING_BAR_LAST: Int = CATEGORY_ORDER_RATING_BAR

        const val ATTRIBUTE_INDICATOR: String = "Is an indicator"
        const val ATTRIBUTE_NUM_STARS: String = "Number of stars"
        const val ATTRIBUTE_RATING: String = "Rating"
        const val ATTRIBUTE_STEP_SIZE: String = "Step size"

        fun <T> InspectorBuilder<T>.RatingBarCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_RATING_BAR, CATEGORY_ORDER_RATING_BAR, allowNulls, block)
        }
    }
}