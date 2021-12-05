package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradient

import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.Shape

@RequiresApi(Build.VERSION_CODES.Q)
internal open class Api29GradientDrawableAttributeInspector : Api24GradientDrawableAttributeInspector() {

    override fun getRingShape(drawable: GradientDrawable): Shape {
        return Shape.Ring(
            innerRadius = drawable.innerRadius,
            innerRadiusRatio = drawable.innerRadiusRatio,
            thickness = drawable.thickness,
            thicknessRatio = drawable.thicknessRatio,
        )
    }
}