package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.os.Build
import android.widget.AbsSeekBar
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.ProgressBarInspectorConfiguration.Companion.CATEGORY_ORDER_PROGRESS_FIRST
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [AbsSeekBar] type.
 */
class AbsSeekBarInspectorConfiguration : InspectorConfiguration<AbsSeekBar> {

    override fun configure(): Iterable<CategoryInspector<AbsSeekBar>> = Inspect {
        AbsSeekBarThumbCategory {
            TintedDrawable(ATTRIBUTE_THUMB) { it.thumb to it.thumbTintList }
            Dimension(ATTRIBUTE_THUMB_OFFSET) { it.thumbOffset }
        }
        AbsSeekBarTickCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TintedDrawable(ATTRIBUTE_TICK) { it.tickMark to it.tickMarkTintList }
            } else {
                ApiRestricted(ATTRIBUTE_TICK, Build.VERSION_CODES.N)
            }
        }
        AbsSeekBarOtherCategory {
            Boolean(ATTRIBUTE_SPLIT_TRACK) { it.splitTrack }
            Int(ATTRIBUTE_KEY_PROGRESS_INCREMENT) { it.keyProgressIncrement }
        }
    }

    companion object {

        const val CATEGORY_ABS_SEEK_BAR_THUMB = "AbsSeekBar: Thumb"
        const val CATEGORY_ABS_SEEK_BAR_TICK = "AbsSeekBar: Tick"
        const val CATEGORY_ABS_SEEK_BAR_OTHER = "AbsSeekBar: Other"

        const val CATEGORY_ORDER_ABS_SEEK_BAR_FIRST: Int = CATEGORY_ORDER_PROGRESS_FIRST - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_ABS_SEEK_BAR_THUMB: Int = CATEGORY_ORDER_ABS_SEEK_BAR_FIRST
        const val CATEGORY_ORDER_ABS_SEEK_BAR_TICK: Int = CATEGORY_ORDER_ABS_SEEK_BAR_THUMB + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_ABS_SEEK_BAR_OTHER: Int = CATEGORY_ORDER_ABS_SEEK_BAR_TICK + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_ABS_SEEK_BAR_LAST: Int = CATEGORY_ORDER_ABS_SEEK_BAR_OTHER

        const val ATTRIBUTE_THUMB: String = "Thumb"
        const val ATTRIBUTE_THUMB_OFFSET: String = "Thumb offset"
        const val ATTRIBUTE_TICK: String = "Tick"
        const val ATTRIBUTE_SPLIT_TRACK: String = "Whether the track should be split by the thumb"
        const val ATTRIBUTE_KEY_PROGRESS_INCREMENT: String = "The amount to increment or decrement when the user presses the arrow keys"

        fun <T> InspectorBuilder<T>.AbsSeekBarThumbCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_ABS_SEEK_BAR_THUMB, CATEGORY_ORDER_ABS_SEEK_BAR_THUMB, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.AbsSeekBarTickCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_ABS_SEEK_BAR_TICK, CATEGORY_ORDER_ABS_SEEK_BAR_TICK, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.AbsSeekBarOtherCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_ABS_SEEK_BAR_OTHER, CATEGORY_ORDER_ABS_SEEK_BAR_OTHER, allowNulls, block)
        }
    }
}