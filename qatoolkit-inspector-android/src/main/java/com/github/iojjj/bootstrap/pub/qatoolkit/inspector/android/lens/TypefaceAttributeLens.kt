package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.TypedAttributeLens

/**
 * Lens that allows to inspect [Typeface] of [TextView].
 */
class TypefaceAttributeLens : TypedAttributeLens<Typeface> {
    override fun get(view: View): Typeface? {
        return (view as? TextView)?.typeface
    }
}