/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.extension

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import tech.antibytes.gradle.coverage.task.TaskContract
import java.io.File

internal object AndroidExtensionConfigurator : TaskContract.AndroidExtensionConfigurator {
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

    private fun <T : CommonExtension<*, *, *, *>> setupAndroidTest(
        project: Project,
        extension: T,
    ) {
        extension.testOptions {
            unitTests {
                all {
                    // see: https://github.com/gradle/kotlin-dsl-samples/issues/440
                    it.jvmArgs("-noverify", "-ea")

                    val jacocoTaskExtension = it.extensions.getByType(JacocoTaskExtension::class.java)
                    // see: https://stackoverflow.com/questions/48945710/after-upgrade-of-gradle-to-the-version-3-0-1-something-generates-redundant-jacoc
                    // AGP 7.0.x location: "outputs/unit_test_code_coverage/${configuration.flavour}${configuration.variant.capitalize()}UnitTest/test${configuration.flavour.capitalize()}${configuration.variant.capitalize()}UnitTest.exec"
                    // AGP 4.2.x location: "../jacoco/jacoco.exec"
                    val infix = it.name.removePrefix("test").removeSuffix("UnitTest").decapitalize()
                    jacocoTaskExtension.setDestinationFile(
                        File("${project.buildDir.path}${File.separator}jacoco${File.separator}$infix.exec")
                    )

                    jacocoTaskExtension.isIncludeNoLocationClasses = true

                    // see: https://issuetracker.google.com/issues/178015739?pli=1
                    jacocoTaskExtension.excludes = listOf("jdk.internal.*", "kotlin.*", "com.library.*")
                }
            }
        }
    }

    override fun configure(project: Project) {
        resolveAndroidExtension(project) { extension ->
            setupAndroidTest(
                project = project,
                extension = extension,
            )
        }
    }
}
