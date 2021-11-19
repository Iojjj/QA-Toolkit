package com.github.iojjj.bootstrap.qatoolkit.settings

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager.Companion.canDrawOverlays
import com.github.iojjj.bootstrap.qatoolkit.theme.QaToolkitTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf
import com.google.accompanist.insets.rememberInsetsPaddingValues

class SettingsActivity : AppCompatActivity() {

    private val canDrawOverlaysState = mutableStateOf(false)
    private val isToolkitServiceEnabledState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QaToolkitTheme {
                ProvideWindowInsets {
                    val windowInsets = LocalWindowInsets.current
                    val insets = derivedWindowInsetsTypeOf(windowInsets.systemBars)
                    val padding = rememberInsetsPaddingValues(insets)
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "QA Toolkit Setup Wizard",
                                    )
                                },
                            )
                        },
                        content = {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                            ) {
                                val canDrawOverlays = canDrawOverlaysState.value
                                val isToolkitServiceEnabled = isToolkitServiceEnabledState.value
                                Step1(canDrawOverlays)
                                if (canDrawOverlays) {
                                    Step2(isToolkitServiceEnabled)
                                }
                                if (canDrawOverlays && isToolkitServiceEnabled) {
                                    Congratulations()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(bottom = padding.calculateTopPadding())
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        canDrawOverlaysState.value = canDrawOverlays(this)
        val accessibilityManager = getSystemService<AccessibilityManager>()!!
        val serviceList = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_VISUAL)
        isToolkitServiceEnabledState.value = serviceList.any { it.resolveInfo.serviceInfo.applicationInfo.packageName == packageName }
    }

    @Composable
    private fun Step1(
        canDrawOverlays: Boolean
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Step 1",
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Grant an access to display over of other apps."
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (canDrawOverlays) {
                    Text(
                        text = "Permission to display over other apps is granted.",
                    )
                } else {
                    Button(
                        onClick = {
                            startActivity(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.fromParts("package", packageName, "")
                                )
                            )
                        }
                    ) {
                        Text(
                            text = "Open Settings"
                        )
                    }
                }
            } else {
                Text(
                    text = "Applications can display over other apps without additional permissions on current Android version."
                )
            }
        }
    }

    @Composable
    private fun Step2(
        isToolkitServiceEnabled: Boolean
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Step 2",
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Enable QA Toolkit accessibility service."
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isToolkitServiceEnabled) {
                Text(
                    text = "QA Toolkit accessibility service is enabled.",
                )
            } else {
                Button(
                    onClick = {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                ) {
                    Text(
                        text = "Open Settings"
                    )
                }
            }
        }
    }

    @Composable
    private fun Congratulations() {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Congratulations",
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "QA Toolkit is properly configured."
            )
        }
    }
}