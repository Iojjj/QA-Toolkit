package com.github.iojjj.bootstrap.pub.qatoolkit.bridge

import android.app.Application
import android.content.Context
import com.github.iojjj.bootstrap.internal.qatoolkit.bridge.QaToolkitLayoutObserver

/**
 * QA Toolkit Bridge object. It can be used to manually start and stop observations.
 */
object QaToolkitBridge {

    /**
     * Starts observation.
     * @param context instance of [Context]
     */
    fun start(context: Context) {
        QaToolkitLayoutObserver.start(context.applicationContext as Application)
    }

    /**
     * Stops observation.
     * @param context instance of [Context]
     */
    fun stop(context: Context) {
        QaToolkitLayoutObserver.stop(context.applicationContext as Application)
    }
}