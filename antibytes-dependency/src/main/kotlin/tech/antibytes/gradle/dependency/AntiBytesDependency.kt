/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project

class AntiBytesDependency : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            "antiBytesDependency",
            AntiBytesDependencyExtension::class.java
        )

        if (!target.plugins.hasPlugin("com.github.ben-manes.versions")) {
            target.plugins.apply("com.github.ben-manes.versions")
        }

        if (!target.plugins.hasPlugin("org.owasp.dependencycheck")) {
            target.plugins.apply("org.owasp.dependencycheck")
        }

        DependencyUpdate.configure(target, extension)
    }
}
