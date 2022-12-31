/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.android

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.ConfigurationContract

internal object CompileTaskConfigurator : ConfigurationContract.Configurator<Unit> {
    override fun configure(project: Project, configuration: Unit) {
        project.tasks.withType<KotlinCompile> {
            if (name.endsWith("Android")) {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_1_8.toString()
                }
            }
        }
    }
}
