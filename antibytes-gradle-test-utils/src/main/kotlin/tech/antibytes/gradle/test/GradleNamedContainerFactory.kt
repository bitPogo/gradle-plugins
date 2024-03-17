/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.test

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.container
import org.gradle.testfixtures.ProjectBuilder

object GradleNamedContainerFactory {
    /**
     * Creates a NamedDomainObjectContainer
     * @param Class<T>: type of the Object which will be wrapped by the resulting NamedDomainObjectContainer
     * @param T: values which will be wrapped by the resulting NamedDomainObjectContainer
     */
    inline fun <reified T : Named> createNamedContainer(
        values: Collection<T>,
    ): NamedDomainObjectContainer<T> {
        val project = ProjectBuilder.builder().build()

        @Suppress("UNCHECKED_CAST")
        return project.container(T::class.java).apply {
            addAll(values)
        }
    }
}
