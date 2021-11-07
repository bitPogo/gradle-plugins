/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import com.appmattus.kotlinfixture.kotlinFixture
import org.gradle.testfixtures.ProjectBuilder

val extensionFixture = kotlinFixture()

inline fun <reified T : Any> createExtension(): T {
    val project = ProjectBuilder.builder().build()

    return project.extensions.create(
        extensionFixture(),
        T::class.java
    )
}
