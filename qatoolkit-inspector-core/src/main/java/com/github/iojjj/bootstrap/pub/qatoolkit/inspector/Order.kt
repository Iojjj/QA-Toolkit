package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

/**
 * Annotation that specifies order in which object inspection happens.
 * @property value Order of inspection. Smaller numbers results in earlier inspection.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Order(val value: Int = 0)
