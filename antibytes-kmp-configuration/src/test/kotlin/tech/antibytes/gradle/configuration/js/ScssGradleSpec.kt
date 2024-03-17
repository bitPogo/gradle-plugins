/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.js

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.npm.PackageJson
import org.junit.jupiter.api.Test

class ScssGradleSpec {
    @Test
    fun `Given enableScss is called it configures in Gradle`() {
        // Given
        val packageJson: PackageJson = mockk(relaxed = true)
        val configuration = slot<Action<PackageJson>>()
        val main: KotlinJsCompilation = mockk {
            every { packageJson(capture(configuration)) } just Runs
        }
        val browserConfig: KotlinJsTargetDsl = mockk {
            every { compilations.getByName("main") } returns main
        }

        // When
        browserConfig.enableScss()
        configuration.captured.execute(packageJson)

        // Then
        verify {
            packageJson.customField(
                "exports",
                mapOf(
                    "./kotlin/scss/*.scss" to mapOf(
                        "import" to "./*.scss",
                        "require" to "./*.scss",
                    ),
                ),
            )
        }
    }
}
