/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradleNamedContainerFactory.createNamedContainer

class JsSourceMapSpec {
    @Test
    fun `Given enableSourceMaps is called it configures the usages of SourceMaps`() {
        // Given
        val main: Kotlin2JsCompile = mockk(relaxed = true)
        val test: Kotlin2JsCompile = mockk(relaxed = true)
        val mainProvider: JSProvider = mockk {
            every { get() } returns main
        }
        val testProvider: JSProvider = mockk {
            every { get() } returns test
        }

        val compilationTargets: List<KotlinJsCompilation> = listOf(
            mockk {
                every { name } returns UUID.randomUUID().toString()
                every { compileTaskProvider } returns mainProvider
            },
            mockk {
                every { name } returns UUID.randomUUID().toString()
                every { compileTaskProvider } returns testProvider
            },
        )
        val jsCompilations: NamedDomainObjectContainer<KotlinJsCompilation> = createNamedContainer(compilationTargets)

        val kotlinTarget: KotlinJsTarget = mockk {
            every { compilations } returns jsCompilations
        }

        // When
        kotlinTarget.enableSourceMaps()

        // Then
        verify { main.kotlinOptions.sourceMap = true }
        verify { main.kotlinOptions.metaInfo = true }

        verify { test.kotlinOptions.sourceMap = true }
        verify { test.kotlinOptions.metaInfo = true }
    }
}

private abstract class JSProvider : TaskProvider<Kotlin2JsCompile> {
    override fun get(): Kotlin2JsCompile {
        TODO("Not yet implemented")
    }
}
