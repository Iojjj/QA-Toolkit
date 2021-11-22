@file:Suppress("FunctionName")

package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.graphics.text.LineBreaker
import android.os.Build
import android.widget.TextView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.asGravityString
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute.Companion.color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.apiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.textDimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewLayoutCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Drawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.FlatMap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Float
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TextDimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [TextView] type.
 */
// Executing after View's inspector
@Order(1 * INSPECTOR_ORDER_STEP)
class TextViewInspectorConfiguration : InspectorConfiguration<TextView> {

    override fun configure(): Iterable<CategoryInspector<TextView>> = Inspect {
        ViewLayoutCategory {
            Dimension(ATTRIBUTE_MIN_WIDTH) { textView ->
                textView.minWidth.takeUnless { it < 0 }
            }
            Dimension(ATTRIBUTE_MIN_HEIGHT) { textView ->
                textView.minHeight.takeUnless { it < 0 }
            }
            Dimension(ATTRIBUTE_MAX_WIDTH) { textView ->
                textView.maxWidth.takeUnless { it < 0 || it == Int.MAX_VALUE }
            }
            Dimension(ATTRIBUTE_MAX_HEIGHT) { textView ->
                textView.maxHeight.takeUnless { it < 0 || it == Int.MAX_VALUE }
            }
        }
        TextViewTextCategory {
            String(ATTRIBUTE_TEXT) { textView ->
                textView.text.takeUnless { it.isNullOrEmpty() }
            }
            TextDimension(ATTRIBUTE_TEXT_SIZE) { it.textSize }
            Float(ATTRIBUTE_TEXT_SCALE_X) { it.textScaleX }
            FlatMap(ATTRIBUTE_AUTO_SIZE) { textView ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (textView.autoSizeTextType == TextView.AUTO_SIZE_TEXT_TYPE_NONE) {
                        emptyList()
                    } else {
                        listOfNotNull(
                            textView.autoSizeMinTextSize.takeUnless { it == -1 }
                                ?.let { textDimension(ATTRIBUTE_AUTO_SIZE_MIN_TEXT_SIZE, it) },
                            textView.autoSizeMaxTextSize.takeUnless { it == -1 }
                                ?.let { textDimension(ATTRIBUTE_AUTO_SIZE_MAX_TEXT_SIZE, it) },
                            textView.autoSizeStepGranularity.takeUnless { it == -1 }
                                ?.let { textDimension(ATTRIBUTE_AUTO_SIZE_STEP_GRANULARITY, it) },
                        )
                    }
                } else {
                    listOf(
                        apiRestricted(
                            ATTRIBUTE_AUTO_SIZE_MIN_TEXT_SIZE,
                            Build.VERSION_CODES.O
                        ),
                        apiRestricted(
                            ATTRIBUTE_AUTO_SIZE_MAX_TEXT_SIZE,
                            Build.VERSION_CODES.O
                        ),
                        apiRestricted(
                            ATTRIBUTE_AUTO_SIZE_STEP_GRANULARITY,
                            Build.VERSION_CODES.O
                        ),
                    )
                }
            }
            Color(ATTRIBUTE_TEXT_COLOR) {
                it.textColors?.let { colors ->
                    colors.getColorForState(it.drawableState, colors.defaultColor)
                }
            }

        }
        TextViewShadowCategory {
            FlatMap(ATTRIBUTE_SHADOW) {
                if (it.shadowRadius <= 0.0f) {
                    emptyList()
                } else {
                    listOf(
                        dimension(ATTRIBUTE_SHADOW_RADIUS, it.shadowRadius),
                        dimension(ATTRIBUTE_SHADOW_DX, it.shadowDx),
                        dimension(ATTRIBUTE_SHADOW_DY, it.shadowDy),
                        color(ATTRIBUTE_SHADOW_COLOR, it.shadowColor),
                    )
                }
            }
        }
        TextViewHintCategory {
            String(ATTRIBUTE_HINT) { it.hint }
            Color(ATTRIBUTE_HINT_COLOR) { it.currentHintTextColor }
        }
        TextViewParagraphCategory {
            Dimension(ATTRIBUTE_LINE_SPACING_EXTRA) { it.lineSpacingExtra }
            Float(ATTRIBUTE_LINE_SPACING_MULTIPLIER) { it.lineSpacingMultiplier }
            Float(ATTRIBUTE_LETTER_SPACING) { it.letterSpacing }
            Int(ATTRIBUTE_MIN_EMS) { textView ->
                textView.minEms.takeUnless { it < 0 }
            }
            Int(ATTRIBUTE_MAX_EMS) { textView ->
                textView.maxEms.takeUnless { it < 0 || it == Int.MAX_VALUE }
            }
            Int(ATTRIBUTE_MIN_LINES) { textView ->
                textView.minLines.takeUnless { it < 0 }
            }
            Int(ATTRIBUTE_MAX_LINES) { textView ->
                textView.maxLines.takeUnless { it < 0 || it == Int.MAX_VALUE }
            }
            Int(ATTRIBUTE_LINE_COUNT) { it.lineCount }
            Dimension(ATTRIBUTE_LINE_HEIGHT) { it.lineHeight }
        }
        TextViewCompoundDrawables {
            Dimension(ATTRIBUTE_COMPOUND_DRAWABLE_PADDING) { it.compoundDrawablePadding }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_START) { it.compoundDrawablesRelative[0] to it.compoundDrawableTintList }
                TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_TOP) { it.compoundDrawablesRelative[1] to it.compoundDrawableTintList }
                TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_END) { it.compoundDrawablesRelative[2] to it.compoundDrawableTintList }
                TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM) { it.compoundDrawablesRelative[3] to it.compoundDrawableTintList }
            } else {
                Drawable(ATTRIBUTE_COMPOUND_DRAWABLE_START) { it.compoundDrawablesRelative[0] }
                Drawable(ATTRIBUTE_COMPOUND_DRAWABLE_TOP) { it.compoundDrawablesRelative[1] }
                Drawable(ATTRIBUTE_COMPOUND_DRAWABLE_END) { it.compoundDrawablesRelative[2] }
                Drawable(ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM) { it.compoundDrawablesRelative[3] }
                ApiRestricted("${ATTRIBUTE_COMPOUND_DRAWABLE_START}: Tint", Build.VERSION_CODES.M) {
                    it.compoundDrawablesRelative[0]
                }
                ApiRestricted("${ATTRIBUTE_COMPOUND_DRAWABLE_TOP}: Tint", Build.VERSION_CODES.M) {
                    it.compoundDrawablesRelative[1]
                }
                ApiRestricted("${ATTRIBUTE_COMPOUND_DRAWABLE_END}: Tint", Build.VERSION_CODES.M) {
                    it.compoundDrawablesRelative[2]
                }
                ApiRestricted("${ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM}: Tint", Build.VERSION_CODES.M) {
                    it.compoundDrawablesRelative[3]
                }
            }
        }

        TextViewOtherCategory {
            String(ATTRIBUTE_GRAVITY) { it.gravity.asGravityString() }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String(ATTRIBUTE_JUSTIFICATION_MODE) {
                    when (val mode = it.justificationMode) {
                        LineBreaker.JUSTIFICATION_MODE_INTER_WORD -> "Inter Word"
                        LineBreaker.JUSTIFICATION_MODE_NONE -> "None"
                        else -> "Unsupported mode: $mode"
                    }
                }
            }
            String(ATTRIBUTE_ELLIPSIZE) { it.ellipsize?.name }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {

        const val CATEGORY_TEXT_VIEW_TEXT: String = "Text View: Text"
        const val CATEGORY_TEXT_VIEW_TEXT_SHADOW: String = "Text View: Text Shadow"
        const val CATEGORY_TEXT_VIEW_HINT: String = "Text View: Hint"
        const val CATEGORY_TEXT_VIEW_PARAGRAPH: String = "Text View: Paragraph"
        const val CATEGORY_TEXT_VIEW_COMPOUND_DRAWABLES: String = "Text View: Compound Drawables"
        const val CATEGORY_TEXT_VIEW_OTHER: String = "Text View: Other"

        const val CATEGORY_ORDER_TEXT_VIEW_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_TEXT_VIEW_TEXT: Int = CATEGORY_ORDER_TEXT_VIEW_FIRST
        const val CATEGORY_ORDER_TEXT_VIEW_TEXT_SHADOW: Int = CATEGORY_ORDER_TEXT_VIEW_TEXT + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TEXT_VIEW_HINT: Int = CATEGORY_ORDER_TEXT_VIEW_TEXT_SHADOW + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TEXT_VIEW_PARAGRAPH: Int = CATEGORY_ORDER_TEXT_VIEW_HINT + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TEXT_VIEW_COMPOUND_DRAWABLES: Int = CATEGORY_ORDER_TEXT_VIEW_PARAGRAPH + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TEXT_VIEW_OTHER: Int = CATEGORY_ORDER_TEXT_VIEW_COMPOUND_DRAWABLES + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_TEXT_VIEW_LAST: Int = CATEGORY_ORDER_TEXT_VIEW_OTHER

        const val ATTRIBUTE_TEXT: String = "Text"
        const val ATTRIBUTE_TEXT_SIZE: String = "Text size"
        const val ATTRIBUTE_TEXT_SCALE_X: String = "Text scale X"
        const val ATTRIBUTE_TEXT_COLOR: String = "Text color"
        const val ATTRIBUTE_HINT: String = "Hint"
        const val ATTRIBUTE_HINT_COLOR: String = "Hint color"
        const val ATTRIBUTE_MAX_WIDTH: String = "Max width"
        const val ATTRIBUTE_MAX_HEIGHT: String = "Max height"
        const val ATTRIBUTE_MIN_WIDTH: String = "Min width"
        const val ATTRIBUTE_MIN_HEIGHT: String = "Min height"
        const val ATTRIBUTE_MAX_EMS: String = "Max width in ems"
        const val ATTRIBUTE_MAX_LINES: String = "Max number of lines"
        const val ATTRIBUTE_MIN_EMS: String = "Min width in ems"
        const val ATTRIBUTE_MIN_LINES: String = "Min number of lines"
        const val ATTRIBUTE_LINE_COUNT: String = "Number of lines"
        const val ATTRIBUTE_LINE_HEIGHT: String = "Line height"
        const val ATTRIBUTE_LINE_SPACING_MULTIPLIER: String = "Line spacing multiplier"
        const val ATTRIBUTE_LINE_SPACING_EXTRA: String = "Line spacing extra"
        const val ATTRIBUTE_GRAVITY: String = "Text gravity"
        const val ATTRIBUTE_JUSTIFICATION_MODE: String = "Justification mode"
        const val ATTRIBUTE_ELLIPSIZE: String = "Ellipsize type"
        const val ATTRIBUTE_LETTER_SPACING: String = "Letter-spacing"
        const val ATTRIBUTE_AUTO_SIZE: String = "Auto size"
        const val ATTRIBUTE_AUTO_SIZE_STEP_GRANULARITY: String = "Auto size: step granularity"
        const val ATTRIBUTE_AUTO_SIZE_MIN_TEXT_SIZE: String = "Auto size: min size"
        const val ATTRIBUTE_AUTO_SIZE_MAX_TEXT_SIZE: String = "Auto size: max size"
        const val ATTRIBUTE_COMPOUND_DRAWABLE_START: String = "Start drawable"
        const val ATTRIBUTE_COMPOUND_DRAWABLE_TOP: String = "Top drawable"
        const val ATTRIBUTE_COMPOUND_DRAWABLE_END: String = "End drawable"
        const val ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM: String = "Bottom drawable"
        const val ATTRIBUTE_COMPOUND_DRAWABLE_PADDING: String = "Padding between text and drawables"
        const val ATTRIBUTE_SHADOW: String = "Shadow"
        const val ATTRIBUTE_SHADOW_RADIUS: String = "Shadow radius"
        const val ATTRIBUTE_SHADOW_DX: String = "Shadow dx"
        const val ATTRIBUTE_SHADOW_DY: String = "Shadow dy"
        const val ATTRIBUTE_SHADOW_COLOR: String = "Shadow color"

        fun <T> InspectorBuilder<T>.TextViewTextCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_VIEW_TEXT, CATEGORY_ORDER_TEXT_VIEW_TEXT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.TextViewShadowCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_VIEW_TEXT_SHADOW, CATEGORY_ORDER_TEXT_VIEW_TEXT_SHADOW, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.TextViewHintCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_VIEW_HINT, CATEGORY_ORDER_TEXT_VIEW_HINT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.TextViewParagraphCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_VIEW_PARAGRAPH, CATEGORY_ORDER_TEXT_VIEW_PARAGRAPH, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.TextViewCompoundDrawables(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_VIEW_COMPOUND_DRAWABLES, CATEGORY_ORDER_TEXT_VIEW_COMPOUND_DRAWABLES, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.TextViewOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_TEXT_VIEW_OTHER, CATEGORY_ORDER_TEXT_VIEW_OTHER, allowNulls, block)
        }
    }
}