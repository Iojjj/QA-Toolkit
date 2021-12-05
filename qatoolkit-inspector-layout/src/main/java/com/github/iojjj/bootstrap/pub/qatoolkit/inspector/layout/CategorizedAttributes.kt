package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout

import android.os.Parcelable
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import kotlinx.parcelize.Parcelize

/**
 * List of sorted attributes merged into a single category.
 * @property name Category name.
 * @property attributes List of sorted attributes.
 */
@Parcelize
data class CategorizedAttributes(
    val name: String,
    val attributes: List<Attribute>,
) : Parcelable