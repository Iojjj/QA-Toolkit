package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

import org.atteo.classindex.IndexSubclasses

/**
 * Object inspection configuration.
 * @param T Type of inspectable object.
 */
@IndexSubclasses
interface InspectorConfiguration<T> {

    /**
     * Prepare collection of category inspectors.
     * @return collection of category inspectors
     */
    fun configure(): Iterable<CategoryInspector<T>>
}
