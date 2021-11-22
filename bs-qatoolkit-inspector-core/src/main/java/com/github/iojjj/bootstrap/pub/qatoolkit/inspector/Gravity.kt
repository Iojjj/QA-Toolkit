package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

import android.annotation.SuppressLint
import android.view.Gravity

@SuppressLint("RtlHardcoded")
fun Int.asGravityString(): String {
    val result = StringBuilder()
    if (this and Gravity.FILL == Gravity.FILL) {
        result.append("FILL").append(' ')
    } else {
        if (this and Gravity.FILL_VERTICAL == Gravity.FILL_VERTICAL) {
            result.append("FILL_VERTICAL").append(' ')
        } else {
            if (this and Gravity.TOP == Gravity.TOP) {
                result.append("TOP").append(' ')
            }
            if (this and Gravity.BOTTOM == Gravity.BOTTOM) {
                result.append("BOTTOM").append(' ')
            }
        }
        if (this and Gravity.FILL_HORIZONTAL == Gravity.FILL_HORIZONTAL) {
            result.append("FILL_HORIZONTAL").append(' ')
        } else {
            if (this and Gravity.START == Gravity.START) {
                result.append("START").append(' ')
            } else if (this and Gravity.LEFT == Gravity.LEFT) {
                result.append("LEFT").append(' ')
            }
            if (this and Gravity.END == Gravity.END) {
                result.append("END").append(' ')
            } else if (this and Gravity.RIGHT == Gravity.RIGHT) {
                result.append("RIGHT").append(' ')
            }
        }
    }
    if (this and Gravity.CENTER == Gravity.CENTER) {
        result.append("CENTER").append(' ')
    } else {
        if (this and Gravity.CENTER_VERTICAL == Gravity.CENTER_VERTICAL) {
            result.append("CENTER_VERTICAL").append(' ')
        }
        if (this and Gravity.CENTER_HORIZONTAL == Gravity.CENTER_HORIZONTAL) {
            result.append("CENTER_HORIZONTAL").append(' ')
        }
    }
    if (result.isEmpty()) {
        result.append("NO GRAVITY").append(' ')
    }
    if (this and Gravity.DISPLAY_CLIP_VERTICAL == Gravity.DISPLAY_CLIP_VERTICAL) {
        result.append("DISPLAY_CLIP_VERTICAL").append(' ')
    }
    if (this and Gravity.DISPLAY_CLIP_HORIZONTAL == Gravity.DISPLAY_CLIP_HORIZONTAL) {
        result.append("DISPLAY_CLIP_HORIZONTAL").append(' ')
    }
    result.deleteCharAt(result.length - 1)
    return result.toString()
}