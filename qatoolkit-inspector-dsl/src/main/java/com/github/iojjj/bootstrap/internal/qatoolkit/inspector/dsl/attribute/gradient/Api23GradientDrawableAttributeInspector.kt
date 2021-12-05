package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient.GradientDrawableAttributeInspector.Companion.GRADIENT_DRAWABLE_STATE_FIELD_COLORS_API_23

@RequiresApi(Build.VERSION_CODES.M)
internal open class Api23GradientDrawableAttributeInspector : Api21GradientDrawableAttributeInspector() {

    override fun inspectColors(drawable: GradientDrawable): Property<GradientColors> {
        return inspectColors(drawable, GRADIENT_DRAWABLE_STATE_FIELD_COLORS_API_23)
    }
}