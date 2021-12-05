package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens

import android.view.View
import android.view.ViewGroup
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.TypedAttributeLens

/**
 * Lens that allows to inspect layout params if it's an instance of [MarginLayoutParams][ViewGroup.MarginLayoutParams].
 */
class ViewGroupMarginLayoutParamsLens : TypedAttributeLens<ViewGroup.MarginLayoutParams> {
    override fun get(view: View): ViewGroup.MarginLayoutParams? {
        return view.layoutParams as? ViewGroup.MarginLayoutParams
    }
}