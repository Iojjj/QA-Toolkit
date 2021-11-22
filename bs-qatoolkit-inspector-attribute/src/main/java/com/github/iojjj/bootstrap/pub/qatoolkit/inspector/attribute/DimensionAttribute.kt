package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute

import kotlinx.parcelize.Parcelize

/**
 * Attribute that represents dimension.
 * @property value Dimension value,
 * @property valueUnit Dimension unit.
 * @property displayUnits Array of units related to current dimension.
 */
@Parcelize
data class DimensionAttribute(
    override val name: String,
    val value: Float,
    val valueUnit: DimensionType,
    val displayUnits: Array<DimensionType>
) : Attribute {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DimensionAttribute

        if (name != other.name) return false
        if (value != other.value) return false
        if (valueUnit != other.valueUnit) return false
        if (!displayUnits.contentEquals(other.displayUnits)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + valueUnit.hashCode()
        result = 31 * result + displayUnits.contentHashCode()
        return result
    }

    companion object {

        fun dimension(
            name: String,
            value: Number
        ): DimensionAttribute {
            return DimensionAttribute(name, value.toFloat(), DimensionType.PX, arrayOf(DimensionType.DP, DimensionType.PX))
        }

        fun textDimension(
            name: String,
            value: Number
        ): DimensionAttribute {
            return DimensionAttribute(name, value.toFloat(), DimensionType.PX, arrayOf(DimensionType.SP, DimensionType.PX))
        }
    }
}