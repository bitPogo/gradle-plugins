/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.linter

import com.appmattus.kotlinfixture.kotlinFixture
import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.KotlinGradleExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.AntibytesQualityExtension
import tech.antibytes.gradle.quality.QualityContract.Configurator
import tech.antibytes.gradle.quality.api.LinterConfiguration
import tech.antibytes.gradle.quality.api.PartialLinterConfiguration
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction

class SpotlessSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = Spotless

        assertTrue(configurator is Configurator)
    }

    @Test
    fun `Given configure is called it does nothing if no linter configuration was given`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()
        extension.linter.convention(null)

        // When
        Spotless.configure(project, extension)
    }

    @Test
    fun `Given configure is called it adds the targets and basic rules for the code linter`() {
        // Given
        val includes: Set<String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val kotlin = KotlinExtensionFake(spotless, mockk())

        extension.linter.set(
            LinterConfiguration(
                code = PartialLinterConfiguration(
                    include = includes,
                    exclude = emptySet(),
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            kotlin,
            kotlin,
        ) { probe ->
            @Suppress("UNCHECKED_CAST")
            spotless.kotlin(probe as Action<KotlinExtension>)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = kotlin.targetArguments.toSet(),
            expected = includes,
        )
        assertTrue { kotlin.useEndWithNewline }
        assertTrue { kotlin.useIndentWithSpaces }
        assertTrue { kotlin.useTrimTrailingWhitespace }
        assertEquals(
            actual = kotlin.version,
            expected = "0.50.0",
        )
        verify(exactly = 1) { project.plugins.apply("com.diffplug.spotless") }
    }

    @Test
    fun `Given configure is called it excludes paths for the code linter`() {
        // Given
        val includes: Set<String> = fixture()
        val excludes: Set<String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val kotlin = KotlinExtensionFake(spotless, mockk())

        extension.linter.set(
            LinterConfiguration(
                code = PartialLinterConfiguration(
                    include = includes,
                    exclude = excludes,
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            kotlin,
            kotlin,
        ) { probe ->
            @Suppress("UNCHECKED_CAST")
            spotless.kotlin(probe as Action<KotlinExtension>)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = kotlin.targetArguments.toSet(),
            expected = includes,
        )
        assertEquals(
            actual = kotlin.excluded.toSet(),
            expected = excludes,
        )
    }

    @Test
    fun `Given configure is called it disables rules for the code linter`() {
        // Given
        val includes: Set<String> = fixture()
        val disable: Map<String, String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val rules: KotlinExtension.KotlinFormatExtension = mockk()
        val kotlin = KotlinExtensionFake(spotless, rules)

        val disabled = slot<Map<String, String>>()

        extension.linter.set(
            LinterConfiguration(
                code = PartialLinterConfiguration(
                    include = includes,
                    exclude = emptySet(),
                    disabledRules = disable,
                ),
            ),
        )
        every { rules.editorConfigOverride(capture(disabled)) } returns rules

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            kotlin,
            kotlin,
        ) { probe ->
            @Suppress("UNCHECKED_CAST")
            spotless.kotlin(probe as Action<KotlinExtension>)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = disabled.captured,
            expected = disable,
        )
    }

    @Test
    fun `Given configure is called it adds the targets and basic rules for the gradle linter`() {
        // Given
        val includes: Set<String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val gradle: KotlinGradleExtension = mockk(relaxed = true)

        val targets: MutableSet<String> = mutableSetOf()

        extension.linter.set(
            LinterConfiguration(
                gradle = PartialLinterConfiguration(
                    include = includes,
                    exclude = emptySet(),
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every {
            gradle.target(*anyVararg())
        } answers {
            @Suppress("UNCHECKED_CAST")
            targets.addAll(this.args[0] as Array<String>)

            mockk()
        }
        every { gradle.ktlint(any()) } returns mockk()

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            gradle,
            gradle,
        ) { probe ->
            spotless.kotlinGradle(probe)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = targets,
            expected = includes,
        )
        verify(exactly = 1) { project.plugins.apply("com.diffplug.spotless") }
        verify(exactly = 1) { gradle.ktlint("0.50.0") }
        verify(exactly = 1) { gradle.trimTrailingWhitespace() }
        verify(exactly = 1) { gradle.indentWithSpaces() }
        verify(exactly = 1) { gradle.endWithNewline() }
    }

    @Test
    fun `Given configure is called it excludes paths for the gradle linter`() {
        // Given
        val includes: Set<String> = fixture()
        val excludes: Set<String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val gradle: KotlinGradleExtension = mockk(relaxed = true)

        val excluded: MutableSet<String> = mutableSetOf()

        extension.linter.set(
            LinterConfiguration(
                gradle = PartialLinterConfiguration(
                    include = includes,
                    exclude = excludes,
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every {
            gradle.targetExclude(*anyVararg())
        } answers {
            @Suppress("UNCHECKED_CAST")
            excluded.addAll(this.args[0] as Array<String>)

            mockk()
        }
        every { gradle.ktlint(any()) } returns mockk()

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            gradle,
            gradle,
        ) { probe ->
            spotless.kotlinGradle(probe)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = excluded,
            expected = excludes,
        )
    }

    @Test
    fun `Given configure is called it disables rules for the gradle linter`() {
        // Given
        val includes: Set<String> = fixture()
        val disable: Map<String, String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val gradle: KotlinGradleExtension = mockk(relaxed = true)
        val rules: KotlinGradleExtension.KotlinFormatExtension = mockk()

        val disabled = slot<Map<String, String>>()

        extension.linter.set(
            LinterConfiguration(
                gradle = PartialLinterConfiguration(
                    include = includes,
                    exclude = emptySet(),
                    disabledRules = disable,
                ),
            ),
        )
        every { gradle.ktlint(any()) } returns rules
        every { rules.editorConfigOverride(capture(disabled)) } returns rules

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            gradle,
            gradle,
        ) { probe ->
            spotless.kotlinGradle(probe)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = disabled.captured,
            expected = disable,
        )
    }

    @Test
    fun `Given configure is called it adds the targets and basic rules for the Misc linter`() {
        // Given
        val includes: Set<String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val misc: FormatExtension = mockk(relaxed = true)

        val targets: MutableSet<String> = mutableSetOf()

        extension.linter.set(
            LinterConfiguration(
                misc = PartialLinterConfiguration(
                    include = includes,
                    exclude = emptySet(),
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every {
            misc.target(*anyVararg())
        } answers {
            @Suppress("UNCHECKED_CAST")
            targets.addAll(this.args[0] as Array<String>)

            mockk()
        }

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            misc,
            misc,
        ) { probe ->
            spotless.format("misc", probe)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = targets,
            expected = includes,
        )
        verify(exactly = 1) { project.plugins.apply("com.diffplug.spotless") }
        verify(exactly = 1) { misc.trimTrailingWhitespace() }
        verify(exactly = 1) { misc.indentWithSpaces() }
        verify(exactly = 1) { misc.endWithNewline() }
    }

    @Test
    fun `Given configure is called it excludes paths for the Misc linter`() {
        // Given
        val includes: Set<String> = fixture()
        val excludes: Set<String> = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val misc: FormatExtension = mockk(relaxed = true)

        val excluded: MutableSet<String> = mutableSetOf()

        extension.linter.set(
            LinterConfiguration(
                misc = PartialLinterConfiguration(
                    include = includes,
                    exclude = excludes,
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every {
            misc.targetExclude(*anyVararg())
        } answers {
            @Suppress("UNCHECKED_CAST")
            excluded.addAll(this.args[0] as Array<String>)

            mockk()
        }

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            spotless,
            spotless,
        ) { probe ->
            project.extensions.configure(SpotlessExtension::class.java, probe)
        }
        invokeGradleAction(
            misc,
            misc,
        ) { probe ->
            spotless.format("misc", probe)
        }

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = excluded,
            expected = excludes,
        )
    }

    private class KotlinExtensionFake(
        spotlessExtension: SpotlessExtension,
        private val formatExtension: KotlinFormatExtension,
    ) : KotlinExtension(spotlessExtension) {
        var targetArguments: Array<out Any?> = emptyArray()
        var excluded: Array<out Any?> = emptyArray()
        var version: String? = null
        var useTrimTrailingWhitespace = false
        var useIndentWithSpaces = false
        var useEndWithNewline = false

        override fun target(vararg targets: Any?) {
            targetArguments = targets
        }

        override fun targetExclude(vararg targets: Any?) {
            excluded = targets
        }

        override fun ktlint(version: String?): KotlinFormatExtension {
            this.version = version

            return formatExtension
        }

        override fun trimTrailingWhitespace() {
            useTrimTrailingWhitespace = true
        }

        override fun indentWithSpaces() {
            useIndentWithSpaces = true
        }

        override fun endWithNewline() {
            useEndWithNewline = true
        }
    }
}
