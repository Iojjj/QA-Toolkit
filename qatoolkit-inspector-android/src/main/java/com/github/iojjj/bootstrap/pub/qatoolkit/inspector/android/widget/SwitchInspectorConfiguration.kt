package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.Switch
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.CompoundButtonInspectorConfiguration.Companion.CATEGORY_ORDER_COMPOUND_BUTTON_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Drawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [Switch] type.
 */
class SwitchInspectorConfiguration : InspectorConfiguration<Switch> {

    override fun configure(): Iterable<CategoryInspector<Switch>> = Inspect {
        SwitchTextCategory {
            Boolean(ATTRIBUTE_SHOW_TEXT) { it.showText }
            String(ATTRIBUTE_TEXT_ON) { it.textOn }
            String(ATTRIBUTE_TEXT_OFF) { it.textOff }
        }
        SwitchThumbCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TintedDrawable(ATTRIBUTE_THUMB_DRAWABLE) { it.thumbDrawable to it.thumbTintList }
            } else {
                Drawable(ATTRIBUTE_THUMB_DRAWABLE) { it.thumbDrawable }
                ApiRestricted("${ATTRIBUTE_THUMB_DRAWABLE}: Tint", Build.VERSION_CODES.M) {
                    it.thumbDrawable
                }
            }
            Dimension(ATTRIBUTE_THUMB_TEXT_PADDING) { it.thumbTextPadding }
        }
        SwitchTrackCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TintedDrawable(ATTRIBUTE_TRACK_DRAWABLE) { it.trackDrawable to it.trackTintList }
            } else {
                Drawable(ATTRIBUTE_TRACK_DRAWABLE) { it.trackDrawable }
                ApiRestricted("${ATTRIBUTE_TRACK_DRAWABLE}: Tint", Build.VERSION_CODES.M) {
                    it.trackDrawable
                }
            }
        }
        SwitchOtherCategory {
            Dimension(ATTRIBUTE_SWITCH_MIN_WIDTH) { it.switchMinWidth }
            Dimension(ATTRIBUTE_SWITCH_PADDING) { it.switchPadding }
        }
    }

    companion object {

        const val CATEGORY_SWITCH_TEXT: String = "Switch: Text"
        const val CATEGORY_SWITCH_THUMB: String = "Switch: Thumb"
        const val CATEGORY_SWITCH_TRACK: String = "Switch: Track"
        const val CATEGORY_SWITCH_OTHER: String = "Switch: Other"

        const val CATEGORY_ORDER_SWITCH_FIRST: Int = CATEGORY_ORDER_COMPOUND_BUTTON_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_SWITCH_TEXT: Int = CATEGORY_ORDER_SWITCH_FIRST
        const val CATEGORY_ORDER_SWITCH_THUMB: Int = CATEGORY_ORDER_SWITCH_TEXT + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_SWITCH_TRACK: Int = CATEGORY_ORDER_SWITCH_THUMB + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_SWITCH_OTHER: Int = CATEGORY_ORDER_SWITCH_TRACK + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_SWITCH_LAST: Int = CATEGORY_ORDER_SWITCH_OTHER

        const val ATTRIBUTE_SHOW_TEXT: String = "Will show text"
        const val ATTRIBUTE_SWITCH_MIN_WIDTH: String = "Switch min width"
        const val ATTRIBUTE_SWITCH_PADDING: String = "Padding between switch and text"
        const val ATTRIBUTE_TEXT_ON: String = "Text for ON state"
        const val ATTRIBUTE_TEXT_OFF: String = "Text for OFF state"
        const val ATTRIBUTE_THUMB_TEXT_PADDING: String = "Horizontal padding around text"
        const val ATTRIBUTE_THUMB_DRAWABLE: String = "Thumb drawable"
        const val ATTRIBUTE_TRACK_DRAWABLE: String = "Track drawable"

        fun <T> InspectorBuilder<T>.SwitchTextCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_SWITCH_TEXT, CATEGORY_ORDER_SWITCH_TEXT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.SwitchThumbCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_SWITCH_THUMB, CATEGORY_ORDER_SWITCH_THUMB, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.SwitchTrackCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_SWITCH_TRACK, CATEGORY_ORDER_SWITCH_TRACK, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.SwitchOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_SWITCH_OTHER, CATEGORY_ORDER_SWITCH_OTHER, allowNulls, block)
        }
    }
}