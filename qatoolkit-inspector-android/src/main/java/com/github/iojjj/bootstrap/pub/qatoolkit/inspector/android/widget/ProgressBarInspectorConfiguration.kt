package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.widget.ProgressBar
import androidx.annotation.IdRes
import com.github.iojjj.bootstrap.pub.core.never
import com.github.iojjj.bootstrap.pub.core.whenever
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_LARGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_STEP_SMALL
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.ApiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect

/**
 * Implementation of [InspectorConfiguration] for [ProgressBar] type.
 */
class ProgressBarInspectorConfiguration : InspectorConfiguration<ProgressBar> {

    override fun configure(): Iterable<CategoryInspector<ProgressBar>> = Inspect {
        ProgressCategory {
            Int(ATTRIBUTE_PROGRESS) {
                never(it.isIndeterminate) {
                    it.progress
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Int(ATTRIBUTE_PROGRESS_MIN) { progressBar ->
                    progressBar.min.takeUnless { progressBar.isIndeterminate }
                }
            } else {
                ApiRestricted(ATTRIBUTE_PROGRESS_MIN, Build.VERSION_CODES.O)
            }
            Int(ATTRIBUTE_PROGRESS_MAX) {
                never(it.isIndeterminate) {
                    it.max
                }
            }
            Int(ATTRIBUTE_SECONDARY_PROGRESS) {
                never(it.isIndeterminate) {
                    it.secondaryProgress
                }
            }
        }
        ProgressIndeterminateDrawableCategory {
            Boolean(ATTRIBUTE_INDETERMINATE) { it.isIndeterminate }
            TintedDrawable(ATTRIBUTE_INDETERMINATE_DRAWABLE) {
                whenever(it.isIndeterminate) {
                    it.indeterminateDrawable to it.indeterminateTintList
                }
            }
        }
        ProgressPrimaryDrawableCategory {
            Boolean(ATTRIBUTE_INDETERMINATE) { it.isIndeterminate }
            TintedDrawable(ATTRIBUTE_PROGRESS_DRAWABLE) {
                never(it.isIndeterminate) {
                    it.getTintTarget(android.R.id.progress, shouldFallback = true) to it.progressTintList
                }
            }
        }
        ProgressPrimaryBackgroundCategory {
            TintedDrawable(ATTRIBUTE_PROGRESS_BACKGROUND) {
                never(it.isIndeterminate) {
                    it.getTintTarget(android.R.id.background, shouldFallback = false) to it.progressBackgroundTintList
                }
            }
        }
        ProgressSecondaryDrawableCategory {
            TintedDrawable(ATTRIBUTE_SECONDARY_PROGRESS_DRAWABLE) {
                never(it.isIndeterminate) {
                    it.getTintTarget(android.R.id.secondaryProgress, shouldFallback = false) to it.secondaryProgressTintList
                }
            }
        }
        ProgressLayoutCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Dimension(ATTRIBUTE_MIN_WIDTH) { it.minWidth }
                Dimension(ATTRIBUTE_MAX_WIDTH) { it.maxWidth }
                Dimension(ATTRIBUTE_MIN_HEIGHT) { it.minHeight }
                Dimension(ATTRIBUTE_MAX_HEIGHT) { it.maxHeight }
            } else {
                ApiRestricted(ATTRIBUTE_MIN_WIDTH, Build.VERSION_CODES.Q)
                ApiRestricted(ATTRIBUTE_MAX_WIDTH, Build.VERSION_CODES.Q)
                ApiRestricted(ATTRIBUTE_MIN_HEIGHT, Build.VERSION_CODES.Q)
                ApiRestricted(ATTRIBUTE_MAX_HEIGHT, Build.VERSION_CODES.Q)
            }
        }
    }

    companion object {

        const val CATEGORY_PROGRESS: String = "Progress"
        const val CATEGORY_PROGRESS_INDETERMINATE_DRAWABLE: String = "Progress: Indeterminate"
        const val CATEGORY_PROGRESS_PRIMARY_DRAWABLE: String = "Progress: Primary"
        const val CATEGORY_PROGRESS_PRIMARY_BACKGROUND: String = "Progress: Primary Background"
        const val CATEGORY_PROGRESS_SECONDARY_DRAWABLE: String = "Progress: Secondary"
        const val CATEGORY_PROGRESS_LAYOUT: String = "Progress: Layout"

        const val CATEGORY_ORDER_PROGRESS_FIRST: Int = CATEGORY_ORDER_VIEW_BACKGROUND - CATEGORY_ORDER_STEP_LARGE
        const val CATEGORY_ORDER_PROGRESS: Int = CATEGORY_ORDER_PROGRESS_FIRST
        const val CATEGORY_ORDER_PROGRESS_INDETERMINATE_DRAWABLE: Int = CATEGORY_ORDER_PROGRESS + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_PROGRESS_PRIMARY_DRAWABLE: Int = CATEGORY_ORDER_PROGRESS_INDETERMINATE_DRAWABLE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_PROGRESS_PRIMARY_BACKGROUND: Int = CATEGORY_ORDER_PROGRESS_PRIMARY_DRAWABLE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_PROGRESS_SECONDARY_DRAWABLE: Int = CATEGORY_ORDER_PROGRESS_PRIMARY_BACKGROUND + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_PROGRESS_LAYOUT: Int = CATEGORY_ORDER_PROGRESS_SECONDARY_DRAWABLE + CATEGORY_ORDER_STEP_SMALL
        const val CATEGORY_ORDER_PROGRESS_LAST: Int = CATEGORY_ORDER_PROGRESS_LAYOUT

        const val ATTRIBUTE_MIN_WIDTH: String = "Min width"
        const val ATTRIBUTE_MIN_HEIGHT: String = "Min height"
        const val ATTRIBUTE_MAX_WIDTH: String = "Max width"
        const val ATTRIBUTE_MAX_HEIGHT: String = "Max height"
        const val ATTRIBUTE_INDETERMINATE_DRAWABLE: String = "Indeterminate drawable"
        const val ATTRIBUTE_PROGRESS_DRAWABLE: String = "Progress drawable"
        const val ATTRIBUTE_PROGRESS_BACKGROUND: String = "Progress background drawable"
        const val ATTRIBUTE_SECONDARY_PROGRESS_DRAWABLE: String = "Secondary progress drawable"
        const val ATTRIBUTE_PROGRESS: String = "Progress"
        const val ATTRIBUTE_PROGRESS_MIN: String = "Progress min"
        const val ATTRIBUTE_PROGRESS_MAX: String = "Progress max"
        const val ATTRIBUTE_SECONDARY_PROGRESS: String = "Secondary progress"
        const val ATTRIBUTE_INDETERMINATE: String = "Is progress in indeterminate mode"

        fun <T> InspectorBuilder<T>.ProgressCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_PROGRESS, CATEGORY_ORDER_PROGRESS, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ProgressIndeterminateDrawableCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_PROGRESS_INDETERMINATE_DRAWABLE, CATEGORY_ORDER_PROGRESS_INDETERMINATE_DRAWABLE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ProgressPrimaryDrawableCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_PROGRESS_PRIMARY_DRAWABLE, CATEGORY_ORDER_PROGRESS_PRIMARY_DRAWABLE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ProgressPrimaryBackgroundCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_PROGRESS_PRIMARY_BACKGROUND, CATEGORY_ORDER_PROGRESS_PRIMARY_BACKGROUND, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ProgressSecondaryDrawableCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_PROGRESS_SECONDARY_DRAWABLE, CATEGORY_ORDER_PROGRESS_SECONDARY_DRAWABLE, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.ProgressLayoutCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_PROGRESS_LAYOUT, CATEGORY_ORDER_PROGRESS_LAYOUT, allowNulls, block)
        }

        private fun ProgressBar.getTintTarget(
            @IdRes layerId: Int,
            shouldFallback: Boolean
        ): Drawable? {
            var layer: Drawable? = null
            val d = progressDrawable
            if (d != null) {
                progressDrawable = d.mutate()
                if (d is LayerDrawable) {
                    layer = d.findDrawableByLayerId(layerId)
                }
                if (shouldFallback && layer == null) {
                    layer = d
                }
            }
            return layer
        }
    }
}