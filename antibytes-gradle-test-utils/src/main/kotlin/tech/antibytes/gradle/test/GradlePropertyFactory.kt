/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.test

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.testfixtures.ProjectBuilder

/**
 * Helper for GradleProperties
 */
object GradlePropertyFactory {
    private val project = ProjectBuilder.builder().build()

    /**
     * Creates a Property
     * @param Class<T>: type of the Object which will be wrapped by the resulting Property
     * @param T: value of the Object which will be wrapped by the resulting Property
     */
    fun <T> makeProperty(type: Class<T>, value: T?): Property<T> {
        return project.objects.property(type).convention(value)
    }

    /**
     * Creates a ListProperty
     * @param Class<T>: type of the Object which will be wrapped by the resulting Property
     * @param List<T>: value of the Object which will be wrapped by the resulting Property
     */
    fun <T> makeListProperty(type: Class<T>, value: List<T>): ListProperty<T> {
        return project.objects.listProperty(type).convention(value)
    }

    /**
     * Creates a SetProperty
     * @param Class<T>: type of the Object which will be wrapped by the resulting Property
     * @param Set<T>: value of the Object which will be wrapped by the resulting Property
     */
    fun <T> makeSetProperty(type: Class<T>, value: Set<T>): SetProperty<T> {
        return project.objects.setProperty(type).convention(value)
    }

    /**
     * Creates a MapProperty
     * @param Class<T>: type of the Objects Key which will be wrapped by the resulting Property
     * * @param Class<T>: type of the Objects Value which will be wrapped by the resulting Property
     * @param Map<T>: key-value pairs of the Object which will be wrapped by the resulting Property
     */
    fun <T : Any, V : Any> makeMapProperty(keyType: Class<T>, valueType: Class<V>, map: Map<T, V>): MapProperty<T, V> {
        return project.objects.mapProperty(
            keyType,
            valueType,
        ).convention(map)
    }
}
