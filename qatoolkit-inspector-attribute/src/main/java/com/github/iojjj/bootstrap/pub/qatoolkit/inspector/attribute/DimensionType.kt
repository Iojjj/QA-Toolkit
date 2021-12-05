package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute

import android.util.TypedValue

/**
 * Dimension type.
 * @property typedValueUnit Typed of complex unit.
 */
enum class DimensionType(
    val typedValueUnit: Int
) {
    PX(TypedValue.COMPLEX_UNIT_PX),
    DP(TypedValue.COMPLEX_UNIT_DIP),
    SP(TypedValue.COMPLEX_UNIT_SP),
    ;
}