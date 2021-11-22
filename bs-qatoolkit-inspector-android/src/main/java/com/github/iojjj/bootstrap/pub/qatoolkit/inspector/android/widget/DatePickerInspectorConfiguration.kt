package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.text.format.DateUtils
import android.widget.DatePicker
import com.github.iojjj.bootstrap.pub.core.text.asDateString
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [DatePicker] type.
 */
class DatePickerInspectorConfiguration : InspectorConfiguration<DatePicker> {

    @Suppress("DEPRECATION")
    override fun configure(): Iterable<CategoryInspector<DatePicker>> = Inspect {
        DatePickerSelectedDateCategory {
            Int(ATTRIBUTE_DAY_OF_MONTH) { it.dayOfMonth }
            String(ATTRIBUTE_MONTH) {
                DateUtils.getMonthString(it.month, DateUtils.LENGTH_LONG)
            }
            Int(ATTRIBUTE_YEAR) { it.year }
        }
        DatePickerConfigurationCategory {
            String(ATTRIBUTE_FIRST_DAY_OF_WEEK) {
                DateUtils.getDayOfWeekString(it.firstDayOfWeek, DateUtils.LENGTH_LONG)
            }
            String(ATTRIBUTE_MIN_DATE) { it.minDate.asDateString() }
            String(ATTRIBUTE_MAX_DATE) { it.maxDate.asDateString() }
        }
        DatePickerOtherCategory {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Boolean(ATTRIBUTE_CALENDAR_SHOWN) { it.calendarViewShown }
                Boolean(ATTRIBUTE_SPINNERS_SHOWN) { it.spinnersShown }
            }
        }
    }

    companion object {

        const val CATEGORY_DATE_PICKER_SELECTED_DATE: String = "Date Picker: Selected Date"
        const val CATEGORY_DATE_PICKER_CONFIGURATION: String = "Date Picker: Configuration"
        const val CATEGORY_DATE_PICKER_OTHER: String = "Date Picker: Other"

        const val CATEGORY_ORDER_DATE_PICKER_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_DATE_PICKER_SELECTED_DATE: Int = CATEGORY_ORDER_DATE_PICKER_FIRST
        const val CATEGORY_ORDER_DATE_PICKER_CONFIGURATION: Int = CATEGORY_ORDER_DATE_PICKER_SELECTED_DATE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_DATE_PICKER_OTHER: Int = CATEGORY_ORDER_DATE_PICKER_CONFIGURATION + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_DATE_PICKER_LAST: Int = CATEGORY_ORDER_DATE_PICKER_OTHER

        const val ATTRIBUTE_DAY_OF_MONTH: String = "Day of month"
        const val ATTRIBUTE_MONTH: String = "Month"
        const val ATTRIBUTE_YEAR: String = "Year"
        const val ATTRIBUTE_FIRST_DAY_OF_WEEK: String = "First day of week"
        const val ATTRIBUTE_MIN_DATE: String = "Min date"
        const val ATTRIBUTE_MAX_DATE: String = "Max date"
        const val ATTRIBUTE_CALENDAR_SHOWN: String = "Is calendar shown"
        const val ATTRIBUTE_SPINNERS_SHOWN: String = "Is spinners shown"

        fun <T> InspectorBuilder<T>.DatePickerSelectedDateCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_DATE_PICKER_SELECTED_DATE, CATEGORY_ORDER_DATE_PICKER_SELECTED_DATE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.DatePickerConfigurationCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_DATE_PICKER_CONFIGURATION, CATEGORY_ORDER_DATE_PICKER_CONFIGURATION, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.DatePickerOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_DATE_PICKER_OTHER, CATEGORY_ORDER_DATE_PICKER_OTHER, allowNulls, block)
        }
    }
}