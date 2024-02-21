/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.kmp.config.MainConfig

internal object ToolChainConfigurator : ConfigurationContract.ParameterlessConfigurator {
    override fun configure(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) {
            if (name.endsWith("KotlinJvm")) {
                val launcher = project.extensions.getByType(JavaToolchainService::class.java).launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(MainConfig.javaVersionJvm))
                }

                this.kotlinJavaToolchain.toolchain.use(launcher)
            }
        }
    }
}
