package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

/**
 * Sortable object.
 * @property name Object name.
 * @property order Sort order.
 */
interface Sortable : Comparable<Sortable> {
    val name: String
    val order: Int

    override fun compareTo(other: Sortable): Int {
        val compareByOrder = order.compareTo(other.order)
        return if (compareByOrder == 0) {
            name.compareTo(other.name, ignoreCase = true)
        } else {
            compareByOrder
        }
    }
}