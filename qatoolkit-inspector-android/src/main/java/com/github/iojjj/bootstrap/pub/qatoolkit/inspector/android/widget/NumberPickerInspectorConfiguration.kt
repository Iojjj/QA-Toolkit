package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.NumberPicker
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
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TextDimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [NumberPicker] type.
 */
class NumberPickerInspectorConfiguration : InspectorConfiguration<NumberPicker> {

    override fun configure(): Iterable<CategoryInspector<NumberPicker>> = Inspect {
        NumberPickerValueCategory {
            Int(ATTRIBUTE_VALUE) { it.value }
            Int(ATTRIBUTE_MIN_VALUE) { it.minValue }
            Int(ATTRIBUTE_MAX_VALUE) { it.maxValue }
            String(ATTRIBUTE_DISPLAYED_VALUE) { numberPicker ->
                numberPicker.displayedValues.takeUnless { it.isNullOrEmpty() }
                    ?.let { it[numberPicker.value] }
            }
            String(ATTRIBUTE_DISPLAYED_VALUES) { numberPicker ->
                numberPicker.displayedValues.takeUnless { it.isNullOrEmpty() }
                    ?.joinToString()
            }
        }
        NumberPickerTextCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                TextDimension(ATTRIBUTE_TEXT_SIZE) { it.textSize }
                Color(ATTRIBUTE_TEXT_COLOR) { it.textColor }
            } else {
                ApiRestricted(ATTRIBUTE_TEXT_SIZE, Build.VERSION_CODES.Q)
                ApiRestricted(ATTRIBUTE_TEXT_COLOR, Build.VERSION_CODES.Q)
            }
        }
        NumberPickerOtherCategory {
            Boolean(ATTRIBUTE_WRAP_SELECTOR_WHEEL) { it.wrapSelectorWheel }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Dimension(ATTRIBUTE_SELECTION_DIVIDER_HEIGHT) { it.selectionDividerHeight }
            } else {
                ApiRestricted(ATTRIBUTE_SELECTION_DIVIDER_HEIGHT, Build.VERSION_CODES.Q)
            }
        }
    }

    companion object {

        const val CATEGORY_NUMBER_PICKER_VALUE: String = "Number Picker: Value"
        const val CATEGORY_NUMBER_PICKER_TEXT: String = "Number Picker: Text"
        const val CATEGORY_NUMBER_PICKER_OTHER: String = "Number Picker: Other"

        const val CATEGORY_ORDER_NUMBER_PICKER_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_NUMBER_PICKER_VALUE: Int = CATEGORY_ORDER_NUMBER_PICKER_FIRST
        const val CATEGORY_ORDER_NUMBER_PICKER_TEXT: Int = CATEGORY_ORDER_NUMBER_PICKER_VALUE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_NUMBER_PICKER_OTHER: Int = CATEGORY_ORDER_NUMBER_PICKER_TEXT + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_NUMBER_PICKER_LAST: Int = CATEGORY_ORDER_NUMBER_PICKER_OTHER

        const val ATTRIBUTE_VALUE: String = "Selected number"
        const val ATTRIBUTE_DISPLAYED_VALUE: String = "Displayed value"
        const val ATTRIBUTE_DISPLAYED_VALUES: String = "All values"
        const val ATTRIBUTE_MIN_VALUE: String = "Min number"
        const val ATTRIBUTE_MAX_VALUE: String = "Max number"
        const val ATTRIBUTE_WRAP_SELECTOR_WHEEL: String = "Is selector wheel wraps when reaching the min/max value"
        const val ATTRIBUTE_SELECTION_DIVIDER_HEIGHT: String = "Selection divider height"
        const val ATTRIBUTE_TEXT_SIZE: String = "Text size"
        const val ATTRIBUTE_TEXT_COLOR: String = "Text color"

        fun <T> InspectorBuilder<T>.NumberPickerValueCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_NUMBER_PICKER_VALUE, CATEGORY_ORDER_NUMBER_PICKER_VALUE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.NumberPickerTextCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_NUMBER_PICKER_TEXT, CATEGORY_ORDER_NUMBER_PICKER_TEXT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.NumberPickerOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_NUMBER_PICKER_OTHER, CATEGORY_ORDER_NUMBER_PICKER_OTHER, allowNulls, block)
        }
    }
}