package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.TypedAttributeLens

/**
 * Lens that allows to inspect [WebView] settings.
 */
class WebSettingsLens : TypedAttributeLens<WebSettings> {
    override fun get(view: View): WebSettings? {
        return (view as? WebView)?.settings
    }
}