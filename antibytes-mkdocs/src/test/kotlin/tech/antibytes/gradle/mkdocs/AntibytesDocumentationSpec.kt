/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.mkdocs

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import java.io.File
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.Delete
import org.gradle.internal.os.OperatingSystem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.vyarus.gradle.plugin.mkdocs.MkdocsExtension
import ru.vyarus.gradle.plugin.python.PythonExtension
import tech.antibytes.gradle.mkdocs.config.MainConfig
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.VersioningContract

class AntibytesDocumentationSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkStatic(OperatingSystem::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(OperatingSystem::class)
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesDocumentation()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it sets up Python and other dependencies for non MacOs`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val pythonExtension: PythonExtension = mockk(relaxed = true)

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns false
        every { project.extensions.getByType(PythonExtension::class.java) } returns pythonExtension
        every { project.extensions.create(any(), AntibytesDocumentationExtension::class.java) } returns mockk()
        every { OperatingSystem.current() } returns mockk {
            every { isMacOsX } returns false
        }

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 1) { plugins.apply("ru.vyarus.mkdocs") }
        verify(exactly = 1) { plugins.apply("tech.antibytes.gradle.versioning") }
        verify(exactly = 0) { pythonExtension.envPath = any() }
        verify(exactly = 1) {
            pythonExtension.pip(
                MainConfig.includeMarkdown,
                MainConfig.kroki,
                MainConfig.extraData,
                MainConfig.material,
                MainConfig.minify,
                MainConfig.redirects,
                MainConfig.pygments,
                MainConfig.pymdown,
            )
        }
    }

    @Test
    fun `Given apply is called it sets up only Python dependencies for non MacOs`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val pythonExtension: PythonExtension = mockk(relaxed = true)

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true
        every { project.extensions.getByType(PythonExtension::class.java) } returns pythonExtension
        every { project.extensions.create(any(), AntibytesDocumentationExtension::class.java) } returns mockk()
        every { OperatingSystem.current() } returns mockk {
            every { isMacOsX } returns false
        }

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("ru.vyarus.mkdocs") }
        verify(exactly = 0) { plugins.apply("tech.antibytes.gradle.versioning") }
        verify(exactly = 0) { pythonExtension.envPath = any() }
        verify(exactly = 1) {
            pythonExtension.pip(
                MainConfig.includeMarkdown,
                MainConfig.kroki,
                MainConfig.extraData,
                MainConfig.material,
                MainConfig.minify,
                MainConfig.redirects,
                MainConfig.pygments,
                MainConfig.pymdown,
            )
        }
    }

    @Test
    fun `Given apply is called it sets up Python and other dependencies for MacOs`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val pythonExtension: PythonExtension = mockk(relaxed = true)
        val projectDir = File(fixture<String>())

        every { project.plugins } returns plugins
        every { project.rootDir } returns projectDir
        every { plugins.hasPlugin(any<String>()) } returns false
        every { project.extensions.getByType(PythonExtension::class.java) } returns pythonExtension
        every { project.extensions.create(any(), AntibytesDocumentationExtension::class.java) } returns mockk()
        every { OperatingSystem.current() } returns mockk {
            every { isMacOsX } returns true
        }

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 1) { plugins.apply("ru.vyarus.mkdocs") }
        verify(exactly = 1) { plugins.apply("tech.antibytes.gradle.versioning") }
        verify(exactly = 1) {
            pythonExtension.envPath = "${projectDir.absolutePath.trimEnd('/')}/.gradle/python/opt/homebrew/"
        }
        verify(exactly = 1) {
            pythonExtension.pip(
                MainConfig.includeMarkdown,
                MainConfig.kroki,
                MainConfig.extraData,
                MainConfig.material,
                MainConfig.minify,
                MainConfig.redirects,
                MainConfig.pygments,
                MainConfig.pymdown,
            )
        }
    }

    @Test
    fun `Given apply is called it sets up only Python dependencies for MacOs`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val pythonExtension: PythonExtension = mockk(relaxed = true)
        val projectRootDir = File(fixture<String>())

        every { project.plugins } returns plugins
        every { project.rootDir } returns projectRootDir
        every { plugins.hasPlugin(any<String>()) } returns true
        every { project.extensions.getByType(PythonExtension::class.java) } returns pythonExtension
        every { project.extensions.create(any(), AntibytesDocumentationExtension::class.java) } returns mockk()
        every { OperatingSystem.current() } returns mockk {
            every { isMacOsX } returns true
        }

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("ru.vyarus.mkdocs") }
        verify(exactly = 0) { plugins.apply("tech.antibytes.gradle.versioning") }
        verify(exactly = 1) {
            pythonExtension.envPath = "${projectRootDir.absolutePath.trimEnd('/')}/.gradle/python/opt/homebrew/"
        }
        verify(exactly = 1) {
            pythonExtension.pip(
                MainConfig.includeMarkdown,
                MainConfig.kroki,
                MainConfig.extraData,
                MainConfig.material,
                MainConfig.minify,
                MainConfig.redirects,
                MainConfig.pygments,
                MainConfig.pymdown,
            )
        }
    }

    @Test
    fun `Given apply is called it registers a cleanup task`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val cleanUpTask: Delete = mockk(relaxed = true)

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true
        every { project.extensions.getByType(PythonExtension::class.java) } returns mockk(relaxed = true)
        every {
            project.extensions.create(
                any(),
                AntibytesDocumentationExtension::class.java,
            )
        } returns mockk(relaxed = true)

        invokeGradleAction(
            { probe -> project.tasks.register("clean", Delete::class.java, probe) },
            cleanUpTask,
            mockk(),
        )

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 1) {
            cleanUpTask.delete("build")
        }
    }

    @Test
    fun `Given apply is sets up MkDocs for SNAPSHOT Versions`() {
        // Given
        mockkObject(Versioning)

        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val extension = createExtension<AntibytesDocumentationExtension>()
        val versioningConfiguration: VersioningContract.VersioningConfiguration = mockk()
        val version = "${fixture<String>()}-SNAPSHOT"
        val mkDocs: MkdocsExtension = mockk(relaxed = true)
        val projectDir = File(fixture<String>())
        val publishing: MkdocsExtension.Publish = mockk(relaxed = true)

        extension.versioning.set(versioningConfiguration)

        every { project.plugins } returns plugins
        every { project.projectDir } returns projectDir
        every { plugins.hasPlugin(any<String>()) } returns true
        every { project.extensions.getByType(PythonExtension::class.java) } returns mockk(relaxed = true)
        every {
            project.extensions.create(
                any(),
                AntibytesDocumentationExtension::class.java,
            )
        } returns extension
        every { mkDocs.publish } returns publishing

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            project,
        )

        every { project.extensions.getByType(MkdocsExtension::class.java) } returns mkDocs
        every { Versioning.getInstance(any(), any()).versionName() } returns version

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 1) { Versioning.getInstance(project, versioningConfiguration) }
        verify(exactly = 1) { mkDocs.sourcesDir = projectDir.absolutePath }
        verify(exactly = 1) { publishing.docPath = version }
        verify(exactly = 1) { publishing.rootRedirect = false }
        verify(exactly = 1) { publishing.generateVersionsFile = true }
        verify(exactly = 1) { mkDocs.strict = true }
        verify(exactly = 1) { mkDocs.extras = mapOf("version" to version) }

        unmockkObject(Versioning)
    }

    @Test
    fun `Given apply is sets up MkDocs for Versions`() {
        // Given
        mockkObject(Versioning)

        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)
        val extension = createExtension<AntibytesDocumentationExtension>()
        val versioningConfiguration: VersioningContract.VersioningConfiguration = mockk()
        val version: String = fixture()
        val mkDocs: MkdocsExtension = mockk(relaxed = true)
        val projectDir = File(fixture<String>())
        val publishing: MkdocsExtension.Publish = mockk(relaxed = true)

        extension.versioning.set(versioningConfiguration)

        every { project.plugins } returns plugins
        every { project.projectDir } returns projectDir
        every { plugins.hasPlugin(any<String>()) } returns true
        every { project.extensions.getByType(PythonExtension::class.java) } returns mockk(relaxed = true)
        every {
            project.extensions.create(
                any(),
                AntibytesDocumentationExtension::class.java,
            )
        } returns extension
        every { mkDocs.publish } returns publishing

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            project,
        )

        every { project.extensions.getByType(MkdocsExtension::class.java) } returns mkDocs
        every { Versioning.getInstance(any(), any()).versionName() } returns version

        // When
        AntibytesDocumentation().apply(project)

        // Then
        verify(exactly = 1) { Versioning.getInstance(project, versioningConfiguration) }
        verify(exactly = 1) { mkDocs.sourcesDir = projectDir.absolutePath }
        verify(exactly = 1) { publishing.docPath = version }
        verify(exactly = 1) { publishing.rootRedirect = true }
        verify(exactly = 1) { publishing.rootRedirectTo = "latest" }
        verify(exactly = 1) { publishing.setVersionAliases("latest") }
        verify(exactly = 1) { publishing.generateVersionsFile = true }

        verify(exactly = 1) { mkDocs.strict = true }

        verify(exactly = 1) { mkDocs.extras = mapOf("version" to version) }

        unmockkObject(Versioning)
    }
}
