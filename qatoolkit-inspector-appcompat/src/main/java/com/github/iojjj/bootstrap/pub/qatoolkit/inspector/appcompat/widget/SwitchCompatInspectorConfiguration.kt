package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.appcompat.widget

import androidx.appcompat.widget.SwitchCompat
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_SHOW_TEXT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_SWITCH_MIN_WIDTH
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_SWITCH_PADDING
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_TEXT_OFF
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_TEXT_ON
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_THUMB_DRAWABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_THUMB_TEXT_PADDING
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.ATTRIBUTE_TRACK_DRAWABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.SwitchOtherCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.SwitchTextCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.SwitchThumbCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.SwitchInspectorConfiguration.Companion.SwitchTrackCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable

/**
 * Implementation of [InspectorConfiguration] for [SwitchCompat] type.
 */
class SwitchCompatInspectorConfiguration : InspectorConfiguration<SwitchCompat> {

    override fun configure(): Iterable<CategoryInspector<SwitchCompat>> = Inspect {
        SwitchTextCategory {
            Boolean(ATTRIBUTE_SHOW_TEXT) { it.showText }
            String(ATTRIBUTE_TEXT_ON) { it.textOn }
            String(ATTRIBUTE_TEXT_OFF) { it.textOff }
        }
        SwitchThumbCategory() {
            TintedDrawable(ATTRIBUTE_THUMB_DRAWABLE) { it.thumbDrawable to it.thumbTintList }
            Dimension(ATTRIBUTE_THUMB_TEXT_PADDING) { it.thumbTextPadding }
        }
        SwitchTrackCategory {
            TintedDrawable(ATTRIBUTE_TRACK_DRAWABLE) { it.trackDrawable to it.trackTintList }
        }
        SwitchOtherCategory() {
            Dimension(ATTRIBUTE_SWITCH_MIN_WIDTH) { it.switchMinWidth }
            Dimension(ATTRIBUTE_SWITCH_PADDING) { it.switchPadding }
        }
    }
}