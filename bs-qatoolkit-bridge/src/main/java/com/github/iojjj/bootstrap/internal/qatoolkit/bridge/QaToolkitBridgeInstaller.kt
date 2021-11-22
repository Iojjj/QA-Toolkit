package com.github.iojjj.bootstrap.internal.qatoolkit.bridge

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.github.iojjj.bootstrap.pub.qatoolkit.bridge.QaToolkitBridge

/**
 * Implementation of [ContentProvider] that initializes [QaToolkitLayoutObserver] before application's lifecycle starts.
 */
internal class QaToolkitBridgeInstaller : ContentProvider() {

    override fun onCreate(): Boolean {
        QaToolkitBridge.start(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        return null
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}