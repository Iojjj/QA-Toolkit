package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

import android.view.View

/**
 * View lens that returns object of specific type for inspection.
 * @param R Return type.
 */
interface TypedAttributeLens<R> : AttributeLens {

    /**
     * Get object for inspection from given [view].
     * @param view instance of [View]
     * @return object for inspection or `null`
     */
    fun get(view: View): R?
}