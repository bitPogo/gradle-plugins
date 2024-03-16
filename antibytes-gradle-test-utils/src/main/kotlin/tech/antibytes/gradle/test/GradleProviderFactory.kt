/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.test

import kotlin.reflect.KClass
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.testfixtures.ProjectBuilder

object GradleProviderFactory {
    private val project = ProjectBuilder.builder().build()

    fun <T> createProvider(value: T): Provider<T> = project.provider { value }

    inline fun <reified T : Task> createTaskProvider(
        name: String,
        value: KClass<T>,
        vararg parameter: Any?,
        configure: T.() -> Unit = { /* Do Nothing */ },
    ): TaskProvider<T> {
        val project = ProjectBuilder.builder().build()
        project.tasks.create(name, value.java, *parameter).apply(configure)

        return project.tasks.withType(T::class.java).named(name)
    }

    inline fun <reified T : Task> createTaskProvider(
        project: Project,
        name: String,
        value: KClass<T>,
        vararg parameter: Any?,
        configure: T.() -> Unit = { /* Do Nothing */ },
    ): TaskProvider<T> {
        project.tasks.create(name, value.java, *parameter).apply(configure)

        return project.tasks.withType(T::class.java).named(name)
    }
}
