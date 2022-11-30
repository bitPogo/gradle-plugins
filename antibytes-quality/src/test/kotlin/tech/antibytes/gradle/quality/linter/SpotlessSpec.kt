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
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.AntiBytesQualityExtension
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
        val extension = createExtension<AntiBytesQualityExtension>()
        extension.linter.convention(null)

        // When
        Spotless.configure(project, extension)
    }

    @Test
    fun `Given configure is called it adds the targets and basic rules for the code linter`() {
        // Given
        val includes: Set<String> = fixture()
        val extension = createExtension<AntiBytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val kotlin: KotlinExtension = mockk(relaxed = true)

        val targets: MutableSet<String> = mutableSetOf()

        extension.linter.set(
            LinterConfiguration(
                code = PartialLinterConfiguration(
                    include = includes,
                    exclude = emptySet(),
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every {
            kotlin.target(*anyVararg())
        } answers {
            @Suppress("UNCHECKED_CAST")
            targets.addAll(this.args[0] as Array<String>)

            mockk()
        }
        every { kotlin.ktlint(any()) } returns mockk()

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.kotlin(probe) },
            kotlin,
            kotlin,
        )

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = targets,
            expected = includes,
        )
        verify(exactly = 1) { project.plugins.apply("com.diffplug.spotless") }
        verify(exactly = 1) { kotlin.ktlint("0.47.1") }
        verify(exactly = 1) { kotlin.trimTrailingWhitespace() }
        verify(exactly = 1) { kotlin.indentWithSpaces() }
        verify(exactly = 1) { kotlin.endWithNewline() }
    }

    @Test
    fun `Given configure is called it excludes paths for the code linter`() {
        // Given
        val includes: Set<String> = fixture()
        val excludes: Set<String> = fixture()
        val extension = createExtension<AntiBytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val kotlin: KotlinExtension = mockk(relaxed = true)

        val excluded: MutableSet<String> = mutableSetOf()

        extension.linter.set(
            LinterConfiguration(
                code = PartialLinterConfiguration(
                    include = includes,
                    exclude = excludes,
                    disabledRules = emptyMap(),
                ),
            ),
        )

        every {
            kotlin.targetExclude(*anyVararg())
        } answers {
            @Suppress("UNCHECKED_CAST")
            excluded.addAll(this.args[0] as Array<String>)

            mockk()
        }
        every { kotlin.ktlint(any()) } returns mockk()

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.kotlin(probe) },
            kotlin,
            kotlin,
        )

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = excluded,
            expected = excludes,
        )
    }

    @Test
    fun `Given configure is called it disables rules for the code linter`() {
        // Given
        val includes: Set<String> = fixture()
        val disable: Map<String, String> = fixture()
        val extension = createExtension<AntiBytesQualityExtension>()

        val project: Project = mockk()
        val spotless: SpotlessExtension = mockk(relaxed = true)
        val kotlin: KotlinExtension = mockk(relaxed = true)
        val rules: KotlinExtension.KotlinFormatExtension = mockk()

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
        every { kotlin.ktlint(any()) } returns rules
        every { rules.editorConfigOverride(capture(disabled)) } returns rules

        every { project.plugins.apply(any()) } returns mockk()
        invokeGradleAction(
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.kotlin(probe) },
            kotlin,
            kotlin,
        )

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
        val extension = createExtension<AntiBytesQualityExtension>()

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
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.kotlinGradle(probe) },
            gradle,
            gradle,
        )

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = targets,
            expected = includes,
        )
        verify(exactly = 1) { project.plugins.apply("com.diffplug.spotless") }
        verify(exactly = 1) { gradle.ktlint("0.47.1") }
        verify(exactly = 1) { gradle.trimTrailingWhitespace() }
        verify(exactly = 1) { gradle.indentWithSpaces() }
        verify(exactly = 1) { gradle.endWithNewline() }
    }

    @Test
    fun `Given configure is called it excludes paths for the gradle linter`() {
        // Given
        val includes: Set<String> = fixture()
        val excludes: Set<String> = fixture()
        val extension = createExtension<AntiBytesQualityExtension>()

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
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.kotlinGradle(probe) },
            gradle,
            gradle,
        )

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
        val extension = createExtension<AntiBytesQualityExtension>()

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
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.kotlinGradle(probe) },
            gradle,
            gradle,
        )

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
        val extension = createExtension<AntiBytesQualityExtension>()

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
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.format("misc", probe) },
            misc,
            misc,
        )

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
        val extension = createExtension<AntiBytesQualityExtension>()

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
            { probe -> project.extensions.configure(SpotlessExtension::class.java, probe) },
            spotless,
            spotless,
        )
        invokeGradleAction(
            { probe -> spotless.format("misc", probe) },
            misc,
            misc,
        )

        // When
        Spotless.configure(project, extension)

        // Then
        assertEquals(
            actual = excluded,
            expected = excludes,
        )
    }
}
