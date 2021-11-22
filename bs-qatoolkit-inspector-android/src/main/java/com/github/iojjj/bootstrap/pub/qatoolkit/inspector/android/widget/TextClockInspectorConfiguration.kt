package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.widget.TextClock
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.CATEGORY_ORDER_TEXT_VIEW_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [TextClock] type.
 */
class TextClockInspectorConfiguration : InspectorConfiguration<TextClock> {

    override fun configure(): Iterable<CategoryInspector<TextClock>> = Inspect {
        TextClockCategory {
            String(ATTRIBUTE_TIMEZONE) { it.timeZone }
            Boolean(ATTRIBUTE_24_HOUR_MODE_ENABLED) { it.is24HourModeEnabled }
            String(ATTRIBUTE_12_HOUR_FORMAT) { it.format12Hour }
            String(ATTRIBUTE_24_HOUR_FORMAT) { it.format24Hour }
        }
    }

    companion object {

        const val CATEGORY_TEXT_CLOCK: String = "Text Clock"

        // TODO Reorder from text view
        const val CATEGORY_ORDER_TEXT_CLOCK_FIRST: Int = CATEGORY_ORDER_TEXT_VIEW_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_TEXT_CLOCK: Int = CATEGORY_ORDER_TEXT_CLOCK_FIRST
        const val CATEGORY_ORDER_TEXT_CLOCK_LAST: Int = CATEGORY_ORDER_TEXT_CLOCK

        const val ATTRIBUTE_24_HOUR_MODE_ENABLED: String = "Is 24-hour mode enabled"
        const val ATTRIBUTE_TIMEZONE: String = "Timezone"
        const val ATTRIBUTE_12_HOUR_FORMAT: String = "12-hour format"
        const val ATTRIBUTE_24_HOUR_FORMAT: String = "24-hour format"

        fun <T> InspectorBuilder<T>.TextClockCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_CLOCK, CATEGORY_ORDER_TEXT_CLOCK, allowNulls, block)
        }
    }
}