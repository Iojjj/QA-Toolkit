package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.TimePicker
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [TimePicker] type.
 */
class TimePickerInspectorConfiguration : InspectorConfiguration<TimePicker> {

    @Suppress("DEPRECATION")
    override fun configure(): Iterable<CategoryInspector<TimePicker>> = Inspect {
        TimePickerCategory {
            Boolean(ATTRIBUTE_24_HOUR_MODE) { it.is24HourView }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Int(ATTRIBUTE_HOUR) { it.hour }
                Int(ATTRIBUTE_MINUTE) { it.minute }
            } else {
                Int(ATTRIBUTE_HOUR) { it.currentHour }
                Int(ATTRIBUTE_MINUTE) { it.currentMinute }
            }
        }
    }

    companion object {

        const val CATEGORY_TIME_PICKER: String = "Time Picker"

        const val CATEGORY_ORDER_TIME_PICKER_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_TIME_PICKER: Int = CATEGORY_ORDER_TIME_PICKER_FIRST
        const val CATEGORY_ORDER_TIME_PICKER_LAST: Int = CATEGORY_ORDER_TIME_PICKER

        const val ATTRIBUTE_24_HOUR_MODE: String = "Is 24-hour mode enabled"
        const val ATTRIBUTE_HOUR: String = "Hour"
        const val ATTRIBUTE_MINUTE: String = "Minute"

        fun <T> InspectorBuilder<T>.TimePickerCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TIME_PICKER, CATEGORY_ORDER_TIME_PICKER, allowNulls, block)
        }
    }
}