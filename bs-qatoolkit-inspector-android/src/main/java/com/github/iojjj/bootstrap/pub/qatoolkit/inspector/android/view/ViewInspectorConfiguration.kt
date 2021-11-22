package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.view

import android.os.Build
import android.view.View
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_CLICKABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_HEIGHT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_ID
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_LONG_CLICKABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_TYPE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_WIDTH
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Float
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Id
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.MainCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewBackgroundCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewForegroundCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewLayoutCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewStateCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.ViewTransformCategory

/**
 * Implementation of [InspectorConfiguration] for [View] type.
 */
class ViewInspectorConfiguration : InspectorConfiguration<View> {

    override fun configure(): Iterable<CategoryInspector<View>> = Inspect {
        MainCategory {
            Id(ATTRIBUTE_ID)
            String(ATTRIBUTE_TYPE) { view -> view::class.qualifiedName }
        }
        ViewLayoutCategory {
            Dimension(ATTRIBUTE_WIDTH) { it.width }
            Dimension(ATTRIBUTE_HEIGHT) { it.height }
            Dimension(ATTRIBUTE_MINIMUM_WIDTH) { it.minimumWidth }
            Dimension(ATTRIBUTE_MINIMUM_HEIGHT) { it.minimumHeight }
            // Padding
            Dimension(ATTRIBUTE_PADDING_START) { it.paddingStart }
            Dimension(ATTRIBUTE_PADDING_TOP) { it.paddingTop }
            Dimension(ATTRIBUTE_PADDING_END) { it.paddingEnd }
            Dimension(ATTRIBUTE_PADDING_BOTTOM) { it.paddingBottom }
        }
        ViewBackgroundCategory {
            TintedDrawable(ATTRIBUTE_BACKGROUND) { it.background to it.backgroundTintList }
        }
        ViewForegroundCategory {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TintedDrawable(ATTRIBUTE_FOREGROUND) { it.foreground to it.foregroundTintList }
            }
        }
        ViewTransformCategory {
            Dimension(ATTRIBUTE_PIVOT_X) { it.pivotX }
            Dimension(ATTRIBUTE_PIVOT_Y) { it.pivotY }
            Dimension(ATTRIBUTE_TRANSLATION_X) { it.translationX }
            Dimension(ATTRIBUTE_TRANSLATION_Y) { it.translationY }
            Dimension(ATTRIBUTE_TRANSLATION_Z) { it.translationZ }
            String(ATTRIBUTE_ROTATION_X) { "${it.rotationX}°" }
            String(ATTRIBUTE_ROTATION_Y) { "${it.rotationY}°" }
            String(ATTRIBUTE_ROTATION_Z) { "${it.rotation}°" }
            Dimension(ATTRIBUTE_ELEVATION) { it.elevation }
            Float(ATTRIBUTE_SCALE_X) { it.scaleX }
            Float(ATTRIBUTE_SCALE_Y) { it.scaleY }
        }
        ViewStateCategory {
            Boolean(ATTRIBUTE_ENABLED) { it.isEnabled }
            Boolean(ATTRIBUTE_CLICKABLE) { it.isClickable }
            Boolean(ATTRIBUTE_LONG_CLICKABLE) { it.isLongClickable }
            Boolean(ATTRIBUTE_FOCUSABLE) { it.isFocusable }
            Boolean(ATTRIBUTE_FOCUSABLE_IN_TOUCH_MODE) { it.isFocusableInTouchMode }
            Boolean(ATTRIBUTE_FOCUSED) { it.isFocused }
            Boolean(ATTRIBUTE_ACTIVATED) { it.isActivated }
            Boolean(ATTRIBUTE_SELECTED) { it.isSelected }
            Boolean(ATTRIBUTE_HOVERED) { it.isHovered }
            Boolean(ATTRIBUTE_PRESSED) { it.isPressed }
        }
    }

    companion object {


        const val ATTRIBUTE_MINIMUM_WIDTH: String = "Minimum width"
        const val ATTRIBUTE_MINIMUM_HEIGHT: String = "Minimum height"
        const val ATTRIBUTE_PADDING_START: String = "Start padding"
        const val ATTRIBUTE_PADDING_TOP: String = "Top padding"
        const val ATTRIBUTE_PADDING_END: String = "End padding"
        const val ATTRIBUTE_PADDING_BOTTOM: String = "Bottom padding"
        const val ATTRIBUTE_BACKGROUND: String = "Background"
        const val ATTRIBUTE_FOREGROUND: String = "Foreground"
        const val ATTRIBUTE_ENABLED: String = "Is view enabled"

        const val ATTRIBUTE_FOCUSABLE: String = "Is view focusable"
        const val ATTRIBUTE_FOCUSABLE_IN_TOUCH_MODE: String = "Is view focusable in touch mode"
        const val ATTRIBUTE_FOCUSED: String = "Is view focused"
        const val ATTRIBUTE_ACTIVATED: String = "Is view activated"
        const val ATTRIBUTE_SELECTED: String = "Is view selected"
        const val ATTRIBUTE_HOVERED: String = "Is view hovered"
        const val ATTRIBUTE_PRESSED: String = "Is view pressed"
        const val ATTRIBUTE_PIVOT_X: String = "Pivot X"
        const val ATTRIBUTE_PIVOT_Y: String = "Pivot Y"
        const val ATTRIBUTE_TRANSLATION_X: String = "Translation X"
        const val ATTRIBUTE_TRANSLATION_Y: String = "Translation Y"
        const val ATTRIBUTE_TRANSLATION_Z: String = "Translation Z"
        const val ATTRIBUTE_ROTATION_X: String = "Rotation X"
        const val ATTRIBUTE_ROTATION_Y: String = "Rotation Y"
        const val ATTRIBUTE_ROTATION_Z: String = "Rotation Z"
        const val ATTRIBUTE_SCALE_X: String = "Scale X"
        const val ATTRIBUTE_SCALE_Y: String = "Scale Y"
        const val ATTRIBUTE_ELEVATION: String = "Elevation"
    }
}

