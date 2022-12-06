/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.stableapi

import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.antibytes.gradle.quality.Configurator
import tech.antibytes.gradle.quality.QualityApiContract.StableApiConfiguration
import tech.antibytes.gradle.quality.QualityContract.Extension

internal object StableApi : Configurator() {
    private fun Project.configure(configuration: StableApiConfiguration) {
        extensions.configure(ApiValidationExtension::class.java) {
            ignoredProjects.addAll(configuration.excludeProjects)
            ignoredPackages.addAll(configuration.excludePackages)
            ignoredClasses.addAll(configuration.excludeClasses)
            nonPublicMarkers.addAll(configuration.nonPublicMarkers)
        }
    }

    private fun Project.enableExplictApi() {
        extensions.findByType(KotlinJvmProjectExtension::class.java)?.apply {
            explicitApi()
        }
        extensions.findByType(KotlinAndroidProjectExtension::class.java)?.apply {
            explicitApi()
        }
        extensions.findByType(KotlinJsProjectExtension::class.java)?.apply {
            explicitApi()
        }
        extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
            explicitApi()
        }
    }

    private fun Project.enableExplictApi(excludedProjects: Set<String>) {
        subprojects.forEach { subproject ->
            if (subproject.name !in excludedProjects) {
                subproject.enableExplictApi()
            }
        }
    }

    override fun configure(project: Project, configuration: Extension) {
        configuration.stableApi.orNull.applyIfNotNull { stableApiConfiguration ->
            project.plugins.apply("org.jetbrains.kotlinx.binary-compatibility-validator")
            project.configure(stableApiConfiguration)
            project.enableExplictApi(stableApiConfiguration.excludeProjects)
        }
    }
}
