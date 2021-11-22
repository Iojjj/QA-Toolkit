package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens

import android.view.View
import android.view.ViewGroup
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.TypedAttributeLens

/**
 * Lens that allows to inspect layout params of [ViewGroup].
 */
class ViewGroupLayoutParamsLens : TypedAttributeLens<ViewGroup.LayoutParams> {
    override fun get(view: View): ViewGroup.LayoutParams? {
        return view.layoutParams
    }
}