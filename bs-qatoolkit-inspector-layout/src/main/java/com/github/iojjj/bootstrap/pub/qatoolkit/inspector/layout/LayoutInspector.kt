package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout

import android.view.View
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.AttributeInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.AttributeLens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.InspectorConfiguration
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Lens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.Order
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.TypedAttributeLens
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.mergeCategories
import org.atteo.classindex.ClassFilter
import org.atteo.classindex.ClassIndex
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * Object that inspects a single view and returns list of [categorized attributes][CategorizedAttributes].
 */
@Suppress("UNCHECKED_CAST")
object LayoutInspector {

    private val allAttributeInspectors by lazy {
        loadAttributeInspectors<InspectorConfiguration<Any>, Any>(InspectorConfiguration::class)
    }

    /**
     * Inspect given view.
     * @param view instance of [View]
     * @return list of [categorized attributes][CategorizedAttributes].
     */
    fun inspect(view: View): List<CategorizedAttributes> {
        return mergeCategories(getCategories(view))
            .mapNotNull { categoryInspector ->
                val categoryAttributes = categoryInspector.attributeInspectors
                    .flatMap { attributeInspector ->
                        attributeInspector.inspect(view)
                    }
                    // Remove duplicates
                    .associateBy { it.name }
                    .values
                if (categoryAttributes.isEmpty()) {
                    null
                } else {
                    CategorizedAttributes(
                        categoryInspector.name,
                        categoryAttributes.toList()
                    )
                }
            }
            .toList()
    }

    private fun getCategories(
        view: View,
    ): Sequence<CategoryInspector<View>> {
        return allAttributeInspectors.asSequence()
            .filter { it.inspectableType.isInstance(it.lens.get(view)) }
            .flatMap { inspector ->
                inspector.inspectorConfiguration.configure()
                    // Wrap over view inspection.
                    .map { category ->
                        CategoryInspector(
                            name = category.name,
                            order = category.order,
                            attributeInspectors = category.attributeInspectors.map { attribute ->
                                AttributeInspector(
                                    name = attribute.name,
                                    order = attribute.order,
                                    delegate = {
                                        attribute.inspect(inspector.lens.get(it)!!)
                                    }
                                )
                            }
                        )
                    }
            }
    }

    private fun <I : InspectorConfiguration<T>, T> loadAttributeInspectors(
        inspectorConfigurationClass: KClass<out InspectorConfiguration<*>>
    ): Collection<TypeResolvedAttributeInspector<T>> {
        return ClassIndex.getSubclasses(inspectorConfigurationClass.java)
            .let {
                ClassFilter.only()
                    .classes()
                    .withPublicDefaultConstructor()
                    .from(it)
            }
            .asSequence()
            .sortedBy {
                it.getAnnotation(Order::class.java)
                    ?.value
                    ?: 0
            }
            .map { inspectorSubClass ->
                val inspectableType = inspectorSubClass.findInspectableType(inspectorConfigurationClass.java) as Class<T>
                val attributeLens = inspectorSubClass.findLens(inspectableType)
                val attributeInspector = inspectorSubClass.newInstance() as I
                TypeResolvedAttributeInspector(attributeInspector, inspectableType, attributeLens)
            }
            .toList()
    }

    private fun Class<*>.findInspectableType(
        singleTypeParameterClass: Class<*>
    ): Class<*> {
        var type: Class<*>? = this
        var inspectableType: Class<*>?
        do {
            val attributesInspectorInterface = type?.genericInterfaces
                ?.asSequence()
                ?.filterIsInstance<ParameterizedType>()
                ?.firstOrNull { it.rawType == singleTypeParameterClass }
            inspectableType = attributesInspectorInterface?.actualTypeArguments?.get(0) as? Class<*>
            type = type?.superclass
        } while (inspectableType == null && type != null)
        return inspectableType
            ?: throw IllegalStateException("Can't find type attribute in $this, searching for $singleTypeParameterClass")
    }

    private fun <T> Class<*>.findLens(
        inspectableType: Class<T>
    ): TypedAttributeLens<T> {
        val attributeLens = (getAnnotation(Lens::class.java)
            ?.value
            ?.takeUnless { it == AttributeLens::class }
            ?.java
            ?.newInstance()
            ?: ViewLens(inspectableType as Class<out View>))
            as TypedAttributeLens<T>
        val lensType = attributeLens::class.java.findInspectableType(TypedAttributeLens::class.java)
        if (!lensType.isAssignableFrom(inspectableType)) {
            throw IllegalStateException(
                "$name expects ${inspectableType.name}, " +
                    "but latter does not extend ${View::class.qualifiedName}. " +
                    "Annotate $simpleName with @${Lens::class.simpleName} annotation and specify proper " +
                    "${AttributeLens::class.simpleName} implementation that will retrieve ${inspectableType.simpleName} " +
                    "from ${View::class.simpleName}."
            )
        }
        return attributeLens
    }

    private class TypeResolvedAttributeInspector<T>(
        val inspectorConfiguration: InspectorConfiguration<T>,
        val inspectableType: Class<T>,
        val lens: TypedAttributeLens<out T>,
    )

    private class ViewLens(
        private val type: Class<out View>,
    ) : TypedAttributeLens<View> {
        override fun get(view: View): View? {
            return view.takeIf { type.isInstance(view) }
        }
    }
}