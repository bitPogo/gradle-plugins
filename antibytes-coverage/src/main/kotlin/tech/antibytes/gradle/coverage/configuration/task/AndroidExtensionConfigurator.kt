/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration.task

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract

internal object AndroidExtensionConfigurator : ConfigurationContract.AndroidExtensionConfigurator {
    private fun resolveAndroidExtension(
        project: Project,
        action: (CommonExtension<*, *, *, *>) -> Unit
    ) {
        if (project.plugins.findPlugin("com.android.application") is Plugin<*>) {
            project.extensions.configure(ApplicationExtension::class.java) {
                action(this)
            }
        } else {
            project.extensions.configure(LibraryExtension::class.java) {
                action(this)
            }
        }
    }

    private fun setupAndroidTest(
        project: Project,
        extension: CommonExtension<*, *, *, *>
    ) {
        extension.testOptions {
            unitTests {
                all {
                    // see: https://stackoverflow.com/questions/48945710/after-upgrade-of-gradle-to-the-version-3-0-1-something-generates-redundant-jacoc
                    it.systemProperty(
                        "jacoco-agent.destfile",
                        "${project.buildDir.path}/jacoco/jacoco.exec"
                    )
                    // see: https://github.com/gradle/kotlin-dsl-samples/issues/440
                    it.jvmArgs("-noverify", "-ea")

                    val testExtension = it.extensions.getByType(JacocoTaskExtension::class.java)

                    testExtension.isIncludeNoLocationClasses = true

                    // see: https://issuetracker.google.com/issues/178015739?pli=1
                    testExtension.excludes = listOf("jdk.internal.*", "kotlin.*", "com.library.*")
                    testExtension.includes = listOf("com.application.*")
                }
            }
        }
    }

    private fun configureAndroidTests(project: Project) {
        resolveAndroidExtension(project) { extension ->
            setupAndroidTest(project, extension)
        }
    }

    private fun setupBuildTypes(
        buildType: String,
        extension: CommonExtension<*, *, *, *>
    ) {
        extension.buildTypes {
            getByName(buildType) {
                isTestCoverageEnabled = true
            }
        }
    }

    private fun configureAndroidBuildType(project: Project, buildType: String) {
        resolveAndroidExtension(project) { extension ->
            setupBuildTypes(buildType, extension)
        }
    }

    override fun configure(project: Project, configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration) {
        configureAndroidBuildType(project, configuration.variant)
        configureAndroidTests(project)
    }
}
