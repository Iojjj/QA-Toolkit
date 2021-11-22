package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

internal sealed class Property<T> : Parcelable {
    @Parcelize
    data class Known<T>(
        val value: @RawValue T
    ) : Property<T>()

    @Parcelize
    data class Unknown<T>(
        val api: Int,
    ) : Property<T>()
}