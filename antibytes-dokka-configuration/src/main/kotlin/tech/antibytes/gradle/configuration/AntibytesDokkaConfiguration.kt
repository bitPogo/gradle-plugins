/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.docs.DokkaConfigurator
import tech.antibytes.gradle.util.applyIfNotExists

class AntibytesDokkaConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyIfNotExists("org.jetbrains.dokka")
        DokkaConfigurator.configure(target, Unit)
    }
}
