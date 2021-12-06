/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.test

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.testfixtures.ProjectBuilder

/**
 * Helper for GradleProperties
 */
object GradlePropertyBuilder {
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
}
