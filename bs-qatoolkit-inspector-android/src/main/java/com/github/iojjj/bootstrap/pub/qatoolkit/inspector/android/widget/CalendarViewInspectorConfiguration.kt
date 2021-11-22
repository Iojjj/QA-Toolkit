package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.text.format.DateUtils
import android.widget.CalendarView
import com.github.iojjj.bootstrap.pub.core.text.asDateString
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Drawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [CalendarView] type.
 */
class CalendarViewInspectorConfiguration : InspectorConfiguration<CalendarView> {

    @Suppress("DEPRECATION")
    override fun configure(): Iterable<CategoryInspector<CalendarView>> = Inspect {
        CalendarCategory {
            String(ATTRIBUTE_FIRST_DAY_OF_WEEK) {
                DateUtils.getDayOfWeekString(it.firstDayOfWeek, DateUtils.LENGTH_LONG)
            }
            String(ATTRIBUTE_DATE) { it.date.asDateString() }
            String(ATTRIBUTE_MIN_DATE) { it.minDate.asDateString() }
            String(ATTRIBUTE_MAX_DATE) { it.maxDate.asDateString() }
        }
        CalendarOtherCategory {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Color(ATTRIBUTE_FOCUSED_MONTH_DATE_COLOR) { it.focusedMonthDateColor }
                Drawable(ATTRIBUTE_SELECTED_DATE_VERTICAL_BAR) { it.selectedDateVerticalBar }
                Color(ATTRIBUTE_SELECTED_WEEK_BACKGROUND_COLOR) { it.selectedWeekBackgroundColor }
                Boolean(ATTRIBUTE_SHOW_WEEK_NUMBER) { it.showWeekNumber }
                Int(ATTRIBUTE_SHOWN_WEEK_COUNT) { it.shownWeekCount }
                Color(ATTRIBUTE_WEEK_NUMBER_COLOR) { it.weekNumberColor }
                Color(ATTRIBUTE_WEEK_SEPARATOR_LINE_COLOR) { it.weekSeparatorLineColor }
            }
        }
    }

    companion object {

        const val CATEGORY_CALENDAR: String = "Calendar"
        const val CATEGORY_CALENDAR_OTHER: String = "Calendar: Other"

        const val CATEGORY_ORDER_CALENDAR_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_CALENDAR: Int = CATEGORY_ORDER_CALENDAR_FIRST
        const val CATEGORY_ORDER_CALENDAR_OTHER: Int = CATEGORY_ORDER_CALENDAR + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_CALENDAR_LAST: Int = CATEGORY_ORDER_CALENDAR_OTHER

        const val ATTRIBUTE_DATE: String = "Selected date"
        const val ATTRIBUTE_FIRST_DAY_OF_WEEK: String = "First day of week"
        const val ATTRIBUTE_MIN_DATE: String = "Min date"
        const val ATTRIBUTE_MAX_DATE: String = "Max date"
        const val ATTRIBUTE_FOCUSED_MONTH_DATE_COLOR: String = "Focused month date color"
        const val ATTRIBUTE_SELECTED_DATE_VERTICAL_BAR: String =
            "Drawable for the vertical bar shown at the beginning and at the end of the selected date"
        const val ATTRIBUTE_SELECTED_WEEK_BACKGROUND_COLOR: String = "Selected week background color"
        const val ATTRIBUTE_SHOW_WEEK_NUMBER: String = "Will show week number"
        const val ATTRIBUTE_SHOWN_WEEK_COUNT: String = "Number of weeks to be shown"
        const val ATTRIBUTE_WEEK_NUMBER_COLOR: String = "Week numbers color"
        const val ATTRIBUTE_WEEK_SEPARATOR_LINE_COLOR: String = "Color of separator line between weeks"

        fun <T> InspectorBuilder<T>.CalendarCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_CALENDAR, CATEGORY_ORDER_CALENDAR, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.CalendarOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_CALENDAR_OTHER, CATEGORY_ORDER_CALENDAR_OTHER, allowNulls, block)
        }
    }
}