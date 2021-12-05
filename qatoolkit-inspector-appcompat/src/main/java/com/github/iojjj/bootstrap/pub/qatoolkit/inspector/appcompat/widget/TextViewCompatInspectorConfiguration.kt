package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.appcompat.widget

import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.INSPECTOR_ORDER_STEP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_AUTO_SIZE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_AUTO_SIZE_MAX_TEXT_SIZE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_AUTO_SIZE_MIN_TEXT_SIZE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_AUTO_SIZE_STEP_GRANULARITY
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_COMPOUND_DRAWABLE_END
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_COMPOUND_DRAWABLE_START
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.ATTRIBUTE_COMPOUND_DRAWABLE_TOP
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.TextViewCompoundDrawables
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.widget.TextViewInspectorConfiguration.Companion.TextViewTextCategory
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.textDimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.FlatMap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Remove
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TintedDrawable

/**
 * Implementation of [InspectorConfiguration] for [TextView] type.
 */
// Executing after TextView's inspector
@Order(1 * INSPECTOR_ORDER_STEP + 2)
class TextViewCompatInspectorConfiguration : InspectorConfiguration<TextView> {

    override fun configure(): Iterable<CategoryInspector<TextView>> = Inspect {
        TextViewTextCategory {
            FlatMap(ATTRIBUTE_AUTO_SIZE) { textView ->
                TextViewCompat.getAutoSizeTextType(textView)
                if (TextViewCompat.getAutoSizeTextType(textView) == TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE) {
                    emptyList()
                } else {
                    listOfNotNull(
                        TextViewCompat.getAutoSizeMinTextSize(textView).takeUnless { it == -1 }
                            ?.let { textDimension(ATTRIBUTE_AUTO_SIZE_MIN_TEXT_SIZE, it) },
                        TextViewCompat.getAutoSizeMaxTextSize(textView).takeUnless { it == -1 }
                            ?.let { textDimension(ATTRIBUTE_AUTO_SIZE_MAX_TEXT_SIZE, it) },
                        TextViewCompat.getAutoSizeStepGranularity(textView).takeUnless { it == -1 }
                            ?.let { textDimension(ATTRIBUTE_AUTO_SIZE_STEP_GRANULARITY, it) },
                    )
                }
            }
        }
        TextViewCompoundDrawables {
            // Remove any API restricted messages
            Remove("${ATTRIBUTE_COMPOUND_DRAWABLE_START}: Tint")
            Remove("${ATTRIBUTE_COMPOUND_DRAWABLE_TOP}: Tint")
            Remove("${ATTRIBUTE_COMPOUND_DRAWABLE_END}: Tint")
            Remove("${ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM}: Tint")
            // Add compat information
            TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_START) { it.compoundDrawablesRelative[0] to TextViewCompat.getCompoundDrawableTintList(it) }
            TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_TOP) { it.compoundDrawablesRelative[1] to TextViewCompat.getCompoundDrawableTintList(it) }
            TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_END) { it.compoundDrawablesRelative[2] to TextViewCompat.getCompoundDrawableTintList(it) }
            TintedDrawable(ATTRIBUTE_COMPOUND_DRAWABLE_BOTTOM) { it.compoundDrawablesRelative[3] to TextViewCompat.getCompoundDrawableTintList(it) }
        }
    }
}