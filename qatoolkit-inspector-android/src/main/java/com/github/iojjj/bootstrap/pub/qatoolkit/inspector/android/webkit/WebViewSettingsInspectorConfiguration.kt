package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.webkit

import android.os.Build
import android.webkit.WebSettings
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Lens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.lens.WebSettingsLens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.android.webkit.WebViewInspectorConfiguration.Companion.CATEGORY_ORDER_WEB_VIEW_PAGE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.Inspect
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.Boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.String
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute.TextDimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category.Category

/**
 * Implementation of [InspectorConfiguration] for [WebSettings] type.
 */
@Lens(WebSettingsLens::class)
class WebViewSettingsInspectorConfiguration : InspectorConfiguration<WebSettings> {

    override fun configure(): Iterable<CategoryInspector<WebSettings>> = Inspect {
        WebViewJavaScriptSettingsCategory {
            Boolean(ATTRIBUTE_SETTINGS_JAVASCRIPT_ENABLED) { it.javaScriptEnabled }
            Boolean(ATTRIBUTE_SETTINGS_JAVASCRIPT_CAN_OPEN_WINDOWS_AUTOMATICALLY) { it.javaScriptCanOpenWindowsAutomatically }
        }
        WebViewAccessSettingsCategory {
            Boolean(ATTRIBUTE_SETTINGS_ALLOW_CONTENT_ACCESS) { it.allowContentAccess }
            Boolean(ATTRIBUTE_SETTINGS_ALLOW_FILE_ACCESS) { it.allowFileAccess }
            Boolean(ATTRIBUTE_SETTINGS_ALLOW_FILE_ACCESS_FROM_FILE_URLS) { it.allowFileAccessFromFileURLs }
            Boolean(ATTRIBUTE_SETTINGS_ALLOW_UNIVERSAL_FILE_ACCESS_FROM_FILE_URLS) { it.allowUniversalAccessFromFileURLs }
        }
        WebViewApiSettingsCategory {
            Boolean(ATTRIBUTE_SETTINGS_DATABASE_ENABLED) { it.databaseEnabled }
            Boolean(ATTRIBUTE_SETTINGS_DOM_STORAGE_ENABLED) { it.domStorageEnabled }
        }
        WebViewNetworkSettingsCategory {
            Boolean(ATTRIBUTE_SETTINGS_LOADS_IMAGES_AUTOMATICALLY) { it.loadsImagesAutomatically }
            Boolean(ATTRIBUTE_SETTINGS_LOAD_WITH_OVERVIEW_MODE) { it.loadWithOverviewMode }
            Boolean(ATTRIBUTE_SETTINGS_BLOCK_NETWORK_IMAGE) { it.blockNetworkImage }
            Boolean(ATTRIBUTE_SETTINGS_BLOCK_NETWORK_LOADS) { it.blockNetworkLoads }
            String(ATTRIBUTE_SETTINGS_MIXED_CONTENT_MODE) {
                when (val mode = it.mixedContentMode) {
                    WebSettings.MIXED_CONTENT_NEVER_ALLOW -> "Never allow"
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE -> "Compatibility mode"
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW -> "Always allow"
                    else -> "Unsupported mode: $mode"
                }
            }
        }
        WebViewTextSettingsCategory {
            String(ATTRIBUTE_SETTINGS_TEXT_ZOOM) { "${it.textZoom}%" }
            String(ATTRIBUTE_SETTINGS_DEFAULT_TEXT_ENCODING_NAME) { it.defaultTextEncodingName }
        }
        WebViewFontSettingsCategory {
            TextDimension(ATTRIBUTE_SETTINGS_DEFAULT_FONT_SIZE) { it.defaultFontSize }
            TextDimension(ATTRIBUTE_SETTINGS_DEFAULT_FIXED_FONT_SIZE) { it.defaultFixedFontSize }
            TextDimension(ATTRIBUTE_SETTINGS_MINIMUM_FONT_SIZE) { it.minimumFontSize }
            TextDimension(ATTRIBUTE_SETTINGS_MINIMUM_LOGICAL_FONT_SIZE) { it.minimumLogicalFontSize }
            String(ATTRIBUTE_SETTINGS_CURSIVE_FONT_FAMILY) { it.cursiveFontFamily }
            String(ATTRIBUTE_SETTINGS_FANTASY_FONT_FAMILY) { it.fantasyFontFamily }
            String(ATTRIBUTE_SETTINGS_FIXED_FONT_FAMILY) { it.fixedFontFamily }
            String(ATTRIBUTE_SETTINGS_SERIF_FONT_FAMILY) { it.sansSerifFontFamily }
            String(ATTRIBUTE_SETTINGS_SANS_SERIF_FONT_FAMILY) { it.sansSerifFontFamily }
            String(ATTRIBUTE_SETTINGS_STANDARD_FONT_FAMILY) { it.standardFontFamily }
        }
        WebViewOtherSettingsCategory {
            String(ATTRIBUTE_SETTINGS_CACHE_MODE) {
                when (val mode = it.cacheMode) {
                    WebSettings.LOAD_DEFAULT -> {
                        "Default cache usage mode. If the navigation type doesn't impose any specific behavior, " +
                            "use cached resources when they are available and not expired, otherwise load resources from the network."
                    }
                    WebSettings.LOAD_CACHE_ELSE_NETWORK -> {
                        "Use cached resources when they are available, even if they have expired." +
                            "Otherwise load resources from the network."
                    }
                    WebSettings.LOAD_NO_CACHE -> {
                        "Don't use the cache, load from the network."
                    }
                    WebSettings.LOAD_CACHE_ONLY -> {
                        "Don't use the network, load from the cache."
                    }
                    else -> {
                        "Unsupported cache mode: $mode"
                    }
                }
            }
            Boolean(ATTRIBUTE_SETTINGS_DISPLAY_ZOOM_CONTROLS) { it.displayZoomControls }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String(ATTRIBUTE_SETTINGS_FORCE_DARK) {
                    when (val forceDark = it.forceDark) {
                        WebSettings.FORCE_DARK_AUTO -> "Auto"
                        WebSettings.FORCE_DARK_ON -> "On"
                        WebSettings.FORCE_DARK_OFF -> "Off"
                        else -> "Unsupported value: $forceDark"
                    }
                }
            }
            String(ATTRIBUTE_SETTINGS_LAYOUT_ALGORITHM) { it.layoutAlgorithm.name }
            Boolean(ATTRIBUTE_SETTINGS_MEDIA_PLAYBACK_REQUIRES_USER_GESTURE) { it.mediaPlaybackRequiresUserGesture }
            Boolean(ATTRIBUTE_SETTINGS_USE_WIDE_VIEWPORT) { it.useWideViewPort }
            String(ATTRIBUTE_SETTINGS_USER_AGENT_STRING) { it.userAgentString }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Boolean(ATTRIBUTE_SETTINGS_SAFE_BROWSING_ENABLED) { it.safeBrowsingEnabled }
            }
            Boolean(ATTRIBUTE_SETTINGS_BUILT_IN_ZOOM_CONTROLS) { it.builtInZoomControls }
        }
    }

    companion object {

        const val CATEGORY_WEB_VIEW_SETTINGS_JAVASCRIPT = "WebView: JavaScript Settings"
        const val CATEGORY_WEB_VIEW_SETTINGS_ACCESS = "WebView: Access Settings"
        const val CATEGORY_WEB_VIEW_SETTINGS_API = "WebView: API Settings"
        const val CATEGORY_WEB_VIEW_SETTINGS_NETWORK = "WebView: Network Settings"
        const val CATEGORY_WEB_VIEW_SETTINGS_TEXT = "WebView: Text Settings"
        const val CATEGORY_WEB_VIEW_SETTINGS_FONT = "WebView: Font Settings"
        const val CATEGORY_WEB_VIEW_SETTINGS_OTHER = "WebView: Other Settings"

        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_FIRST: Int = CATEGORY_ORDER_WEB_VIEW_PAGE + 10
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_JAVASCRIPT: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_FIRST
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_ACCESS: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_JAVASCRIPT + 10
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_API: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_ACCESS + 10
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_NETWORK: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_API + 10
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_TEXT: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_NETWORK + 100
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_FONT: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_TEXT + 10
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_OTHER: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_FONT + 10
        const val CATEGORY_ORDER_WEB_VIEW_SETTINGS_LAST: Int = CATEGORY_ORDER_WEB_VIEW_SETTINGS_OTHER

        const val ATTRIBUTE_SETTINGS_ALLOW_CONTENT_ACCESS: String = "Allow content access"
        const val ATTRIBUTE_SETTINGS_ALLOW_FILE_ACCESS: String = "Allow file access"
        const val ATTRIBUTE_SETTINGS_ALLOW_FILE_ACCESS_FROM_FILE_URLS: String = "Allow file access from file URLs"
        const val ATTRIBUTE_SETTINGS_ALLOW_UNIVERSAL_FILE_ACCESS_FROM_FILE_URLS: String = "Allow universal file access from file URLs"
        const val ATTRIBUTE_SETTINGS_BLOCK_NETWORK_IMAGE: String = "Is blocking load of images from network"
        const val ATTRIBUTE_SETTINGS_BLOCK_NETWORK_LOADS: String = "Is blocking load of resources from network"
        const val ATTRIBUTE_SETTINGS_BUILT_IN_ZOOM_CONTROLS: String = "Is using built-in zoom mechanism"
        const val ATTRIBUTE_SETTINGS_CACHE_MODE: String = "Cache mode"
        const val ATTRIBUTE_SETTINGS_CURSIVE_FONT_FAMILY: String = "Cursive font family"
        const val ATTRIBUTE_SETTINGS_DATABASE_ENABLED: String = "Is database storage API enabled"
        const val ATTRIBUTE_SETTINGS_DEFAULT_FIXED_FONT_SIZE: String = "Default fixed font size"
        const val ATTRIBUTE_SETTINGS_DEFAULT_FONT_SIZE: String = "Default font size"
        const val ATTRIBUTE_SETTINGS_DEFAULT_TEXT_ENCODING_NAME: String = "Default text encoding"
        const val ATTRIBUTE_SETTINGS_DISPLAY_ZOOM_CONTROLS: String = "Should display zoom controls"
        const val ATTRIBUTE_SETTINGS_DOM_STORAGE_ENABLED: String = "Is DOM storage API enabled"
        const val ATTRIBUTE_SETTINGS_FORCE_DARK: String = "Force dark mode"
        const val ATTRIBUTE_SETTINGS_JAVASCRIPT_CAN_OPEN_WINDOWS_AUTOMATICALLY: String = "Is JavaScript can open windows automatically"
        const val ATTRIBUTE_SETTINGS_JAVASCRIPT_ENABLED: String = "Is JavaScript enabled"
        const val ATTRIBUTE_SETTINGS_LAYOUT_ALGORITHM: String = "Layout algorithm"
        const val ATTRIBUTE_SETTINGS_LOAD_WITH_OVERVIEW_MODE: String = "Is loading pages in overview mode"
        const val ATTRIBUTE_SETTINGS_LOADS_IMAGES_AUTOMATICALLY: String = "Is loading images automatically"
        const val ATTRIBUTE_SETTINGS_MEDIA_PLAYBACK_REQUIRES_USER_GESTURE: String = "Is requires a user gesture to play media"
        const val ATTRIBUTE_SETTINGS_MINIMUM_FONT_SIZE: String = "Minimum font size"
        const val ATTRIBUTE_SETTINGS_MINIMUM_LOGICAL_FONT_SIZE: String = "Minimum logical font size"
        const val ATTRIBUTE_SETTINGS_MIXED_CONTENT_MODE: String = "Mixed content mode"
        const val ATTRIBUTE_SETTINGS_TEXT_ZOOM: String = "Text zoom of the page"
        const val ATTRIBUTE_SETTINGS_USE_WIDE_VIEWPORT: String = "Is supports \"viewport\" HTML tag"
        const val ATTRIBUTE_SETTINGS_USER_AGENT_STRING: String = "User-Agent"
        const val ATTRIBUTE_SETTINGS_FANTASY_FONT_FAMILY: String = "Fantasy font family"
        const val ATTRIBUTE_SETTINGS_FIXED_FONT_FAMILY: String = "Fixed font family"
        const val ATTRIBUTE_SETTINGS_SANS_SERIF_FONT_FAMILY: String = "Sans-serif font family"
        const val ATTRIBUTE_SETTINGS_SERIF_FONT_FAMILY: String = "Serif font family"
        const val ATTRIBUTE_SETTINGS_STANDARD_FONT_FAMILY: String = "Standard family"
        const val ATTRIBUTE_SETTINGS_SAFE_BROWSING_ENABLED: String = "Is safe browsing enabled"

        fun <T> InspectorBuilder<T>.WebViewJavaScriptSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_JAVASCRIPT, CATEGORY_ORDER_WEB_VIEW_SETTINGS_JAVASCRIPT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewAccessSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_ACCESS, CATEGORY_ORDER_WEB_VIEW_SETTINGS_ACCESS, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewApiSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_API, CATEGORY_ORDER_WEB_VIEW_SETTINGS_API, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewNetworkSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_NETWORK, CATEGORY_ORDER_WEB_VIEW_SETTINGS_NETWORK, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewTextSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_TEXT, CATEGORY_ORDER_WEB_VIEW_SETTINGS_TEXT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewFontSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_FONT, CATEGORY_ORDER_WEB_VIEW_SETTINGS_FONT, allowNulls, block)
        }

        fun <T> InspectorBuilder<T>.WebViewOtherSettingsCategory(
            allowNulls: Boolean = this.allowNulls,
            block: CategoryInspectorBuilder<T>.() -> Unit
        ) {
            Category(CATEGORY_WEB_VIEW_SETTINGS_OTHER, CATEGORY_ORDER_WEB_VIEW_SETTINGS_OTHER, allowNulls, block)
        }
    }
}