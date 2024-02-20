/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import tech.antibytes.gradle.configuration.config.MainConfig

class AntibytesJavaConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        val version = JavaVersion.toVersion(MainConfig.javaVersion)

        target.extensions.findByType(JavaPluginExtension::class.java)?.apply {
            toolchain.languageVersion.set(JavaLanguageVersion.of(MainConfig.javaVersion))

            sourceCompatibility = version
            targetCompatibility = version
        }
    }
}
