package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

import kotlin.reflect.KClass

/**
 * Annotation that specifies [lens][AttributeLens] for object inspection.
 * @property value Type of lens used for object inspection.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Lens(val value: KClass<out AttributeLens>)
