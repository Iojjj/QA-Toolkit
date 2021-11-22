package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import android.view.View
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.float
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.int
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to add boolean attribute.
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param boolean boolean attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Boolean(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    boolean: (T) -> Boolean?,
) {
    add(name, order, allowNulls, boolean) {
        listOf(boolean(name, it))
    }
}

/**
 * Builder that allows to add integer attribute.
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param int integer attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Int(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    int: (T) -> Int?,
) {
    add(name, order, allowNulls, int) {
        listOf(int(name, it))
    }
}

/**
 * Builder that allows to add float attribute.
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param float float attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Float(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    float: (T) -> Float?,
) {
    add(name, order, allowNulls, float) {
        listOf(float(name, it))
    }
}

/**
 * Builder that allows to add string attribute.
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param charSequence string attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.String(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    charSequence: (T) -> CharSequence?,
) {
    add(name, order, allowNulls, charSequence) {
        listOf(string(name, it))
    }
}

/**
 * Builder that allows to add view id attribute.
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param id view id attribute extractor
 */
fun <T : View> CategoryInspectorBuilder<T>.Id(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    id: (T) -> Int = { it.id },
) {
    String(name, order, allowNulls) { view ->
        id(view).takeUnless { it == 0 || it == View.NO_ID }
            ?.let { view.context.resources.getResourceEntryName(it) }
    }
}