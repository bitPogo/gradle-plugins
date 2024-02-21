/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.android

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.configuration.android.config.MainConfig

internal object CompileTaskConfigurator : ConfigurationContract.ParameterlessConfigurator {
    override fun configure(project: Project) {
        project.tasks.withType<KotlinCompile> {
            if (name.endsWith("Android")) {
                kotlinOptions {
                    jvmTarget = JavaVersion.toVersion(MainConfig.javaVersion).toString()
                }
            }
        }
    }
}
