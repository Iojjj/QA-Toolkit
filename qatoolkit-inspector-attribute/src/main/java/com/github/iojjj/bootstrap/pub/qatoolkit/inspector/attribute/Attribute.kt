package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute

import android.os.Parcelable

/**
 * View attribute.
 * @property name Attribute name.
 */
sealed interface Attribute : Parcelable {
    val name: String
}