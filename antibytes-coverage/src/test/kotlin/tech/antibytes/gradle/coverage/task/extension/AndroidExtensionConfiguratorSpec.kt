/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.extension

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestOptions
import com.android.build.api.dsl.UnitTestOptions
import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.junit.Test
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.publishing.invokeGradleAction
import java.io.File
import kotlin.test.assertTrue
import org.gradle.api.tasks.testing.Test as GradleTest

class AndroidExtensionConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils AndroidExtensionConfigurator`() {
        val configurator: Any = AndroidExtensionConfigurator

        assertTrue(configurator is TaskContract.AndroidExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, ReporterTask and a Configuration, resolves a ApplicationExtension and configures it with the test settings`() {
        // Given
        val project: Project = mockk()
        val variant: String = fixture()
        val flavour: String = fixture()

        val extensions: ExtensionContainer = mockk()
        val extension: ApplicationExtension = mockk()
        val jacocoExtension: JacocoTaskExtension = mockk()
        val extensionContainer: ExtensionContainer = mockk()
        val testOptions: TestOptions = mockk()
        val unitTestOptions: UnitTestOptions = mockk()
        val test: GradleTest = mockk()
        val buildDir: File = mockk()
        val path = "soup"

        every { project.plugins.findPlugin("com.android.application") } returns mockk()
        every { project.extensions } returns extensions
        every { project.buildDir } returns buildDir
        every { buildDir.path } returns path

        every { extension.testOptions(captureLambda()) } answers {
            lambda<(TestOptions) -> Unit>().invoke(testOptions)
        }

        every { testOptions.unitTests(captureLambda()) } answers {
            lambda<(UnitTestOptions) -> Unit>().invoke(unitTestOptions)
        }

        every { unitTestOptions.all(captureLambda()) } answers {
            lambda<(GradleTest) -> Unit>().invoke(test)
        }

        every {
            test.jvmArgs(any(), any())
        } returns mockk()
        every { test.extensions } returns extensionContainer
        every { test.name } returns "test${flavour.capitalize()}${variant.capitalize()}UnitTest"

        every {
            extensionContainer.getByType(any<Class<*>>())
        } returns jacocoExtension

        every { jacocoExtension.isIncludeNoLocationClasses = any() } just Runs
        every { jacocoExtension.setDestinationFile(any<File>()) } just Runs
        every {
            jacocoExtension.excludes = any()
        } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(ApplicationExtension::class.java, probe) },
            extension
        )

        // When
        AndroidExtensionConfigurator.configure(project)

        // Then
        verify(exactly = 1) { extensionContainer.getByType(JacocoTaskExtension::class.java) }
        verify(exactly = 1) { test.jvmArgs("-noverify", "-ea") }
        verify(exactly = 1) { jacocoExtension.isIncludeNoLocationClasses = true }
        verify(exactly = 1) {
            jacocoExtension.excludes = listOf("jdk.internal.*", "kotlin.*", "com.library.*")
        }
        verify(exactly = 1) {
            jacocoExtension.setDestinationFile(
                File("$path/jacoco/${flavour}${variant.capitalize()}.exec")
            )
        }
    }

    @Test
    fun `Given configure is called with a Project and a Configuration, resolves a LibraryExtension and configures it with the test settings`() {
        // Given
        val project: Project = mockk()
        val variant: String = fixture()

        val extensions: ExtensionContainer = mockk()
        val extension: LibraryExtension = mockk()
        val jacocoExtension: JacocoTaskExtension = mockk()
        val extensionContainer: ExtensionContainer = mockk()
        val testOptions: TestOptions = mockk()
        val unitTestOptions: UnitTestOptions = mockk()
        val test: GradleTest = mockk()
        val buildDir: File = mockk()
        val path: String = fixture()

        every { project.plugins.findPlugin("com.android.application") } returns null
        every { project.extensions } returns extensions
        every { project.buildDir } returns buildDir
        every { buildDir.path } returns path

        every { extension.testOptions(captureLambda()) } answers {
            lambda<(TestOptions) -> Unit>().invoke(testOptions)
        }

        every { testOptions.unitTests(captureLambda()) } answers {
            lambda<(UnitTestOptions) -> Unit>().invoke(unitTestOptions)
        }

        every { unitTestOptions.all(captureLambda()) } answers {
            lambda<(GradleTest) -> Unit>().invoke(test)
        }

        every {
            test.jvmArgs(any(), any())
        } returns mockk()
        every { test.extensions } returns extensionContainer
        every { test.name } returns "test${variant.capitalize()}UnitTest"

        every {
            extensionContainer.getByType(any<Class<*>>())
        } returns jacocoExtension

        every { jacocoExtension.isIncludeNoLocationClasses = any() } just Runs
        every { jacocoExtension.setDestinationFile(any<File>()) } just Runs
        every {
            jacocoExtension.excludes = any()
        } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(LibraryExtension::class.java, probe) },
            extension
        )

        // When
        AndroidExtensionConfigurator.configure(project)

        // Then
        verify(exactly = 1) { extensionContainer.getByType(JacocoTaskExtension::class.java) }
        verify(exactly = 1) { test.jvmArgs("-noverify", "-ea") }
        verify(exactly = 1) { jacocoExtension.isIncludeNoLocationClasses = true }
        verify(exactly = 1) {
            jacocoExtension.excludes = listOf("jdk.internal.*", "kotlin.*", "com.library.*")
        }
        verify(exactly = 1) {
            jacocoExtension.setDestinationFile(
                File("$path/jacoco/$variant.exec")
            )
        }
    }
}
