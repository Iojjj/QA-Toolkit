package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute

import android.os.Build
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat
import java.util.Locale

/**
 * Attribute that represents common value (string, boolean, number, etc.).
 * @property value Attribute value.
 */
@Parcelize
data class CommonAttribute(
    override val name: String,
    val value: String,
) : Attribute {

    companion object {

        private val FLOAT_FORMATTER = DecimalFormat("0.0#")

        fun apiRestricted(
            name: String,
            minSdk: Int
        ): CommonAttribute {
            return string(name, apiRestrictedString(minSdk))
        }

        fun boolean(
            name: String,
            value: Boolean
        ): CommonAttribute {
            return CommonAttribute(
                name, when (value) {
                    true -> "Yes"
                    false -> "No"
                }
            )
        }

        fun int(
            name: String,
            value: Int
        ): CommonAttribute {
            return CommonAttribute(name, value.toString())
        }

        fun float(
            name: String,
            value: Float
        ): CommonAttribute {
            return CommonAttribute(name, FLOAT_FORMATTER.format(value))
        }

        fun string(
            name: String,
            value: CharSequence
        ): CommonAttribute {
            return CommonAttribute(name, value.toString())
        }

        fun apiRestrictedString(minSdk: Int): String {
            val androidName = when (minSdk) {
                in Int.MIN_VALUE until Build.VERSION_CODES.LOLLIPOP -> throw IllegalStateException()
                Build.VERSION_CODES.LOLLIPOP -> "Android 5.0"
                Build.VERSION_CODES.LOLLIPOP_MR1 -> "Android 5.1"
                Build.VERSION_CODES.M -> "Android 6.0"
                Build.VERSION_CODES.N -> "Android 7.0"
                Build.VERSION_CODES.N_MR1 -> "Android 7.1"
                Build.VERSION_CODES.O -> "Android 8.0"
                Build.VERSION_CODES.O_MR1 -> "Android 8.1"
                Build.VERSION_CODES.P -> "Android 9"
                Build.VERSION_CODES.Q -> "Android 10"
                Build.VERSION_CODES.R -> "Android 11"
                else -> "API " + Build.VERSION.SDK_INT
            }
            return "Inspectable on ${androidName}+"
        }

        fun apiRestrictedStringDecapitalize(minSdk: Int): String {
            return apiRestrictedString(minSdk).replaceFirstChar { it.lowercase(Locale.getDefault()) }
        }
    }
}

