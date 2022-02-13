/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.source

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SourceHelperSpec {
    @TempDir
    lateinit var projectDir: File

    private val fixture = kotlinFixture()

    private data class TestFiles(
        val projectDir: File,
        val platformFile: File,
        val commonFile: File,
        val vanillaFile: File
    )

    private fun makeDir(
        parent: File,
        child: String
    ): File {
        return File("${parent.absolutePath}${File.separator}$child").also {
            it.mkdir()
        }
    }

    private fun addProjectDirStructure(main: File): File {
        val kotlin = makeDir(main, "kotlin")
        val domain = makeDir(kotlin, "tech")
        val org = makeDir(domain, "antibytes")
        val namespace = makeDir(org, "test")

        return makeDir(namespace, fixture())
    }

    private fun setUpDirStructure(): TestFiles {
        val source = makeDir(projectDir, "src")

        val kmpPlatform = addProjectDirStructure(makeDir(source, "jvmMain"))
        val common = addProjectDirStructure(makeDir(source, "commonMain"))
        val platform = addProjectDirStructure(makeDir(source, "main"))

        val kmpFile = File(
            "${kmpPlatform.absolutePath}${File.separator}${fixture<String>()}.kt"
        ).also { it.createNewFile() }
        val commonFile = File(
            "${common.absolutePath}${File.separator}${fixture<String>()}.kt"
        ).also { it.createNewFile() }
        val vanillaFile = File(
            "${platform.absolutePath}${File.separator}${fixture<String>()}.kt"
        ).also { it.createNewFile() }

        return TestFiles(
            projectDir,
            kmpFile,
            commonFile,
            vanillaFile
        )
    }

    @Test
    fun `It fulfils SourceHelper`() {
        val helper: Any = SourceHelper

        assertTrue(helper is ConfigurationContract.SourceHelper)
    }

    @Test
    fun `Given resolveSources is called with a Project and a Context it resolves the projects source structure, if it is not KMP`() {
        // Given
        val project: Project = mockk()
        val files = setUpDirStructure()

        every { project.projectDir } returns files.projectDir

        // When
        val resolved = SourceHelper.resolveSources(project, PlatformContext.JVM)

        // Then
        assertEquals(
            actual = resolved,
            expected = setOf(
                files.vanillaFile,
                files.vanillaFile.parentFile, // package
                files.vanillaFile.parentFile.parentFile, // test
                files.vanillaFile.parentFile.parentFile.parentFile, // antibytes
                files.vanillaFile.parentFile.parentFile.parentFile.parentFile, // tech
                files.vanillaFile.parentFile.parentFile.parentFile.parentFile.parentFile, // kotlin
                files.vanillaFile.parentFile.parentFile.parentFile.parentFile.parentFile.parentFile, // main
            )
        )
    }

    @Test
    fun `Given resolveSources is called with a Project and a Context it resolves the projects source structure, if it is KMP`() {
        // Given
        val project: Project = mockk()
        val files = setUpDirStructure()

        every { project.projectDir } returns files.projectDir

        // When
        val resolved = SourceHelper.resolveSources(project, PlatformContext.JVM_KMP)

        // Then
        assertEquals(
            actual = resolved,
            expected = setOf(
                files.commonFile,
                files.commonFile.parentFile, // package
                files.commonFile.parentFile.parentFile, // test
                files.commonFile.parentFile.parentFile.parentFile, // antibytes
                files.commonFile.parentFile.parentFile.parentFile.parentFile, // tech
                files.commonFile.parentFile.parentFile.parentFile.parentFile.parentFile, // kotlin
                files.commonFile.parentFile.parentFile.parentFile.parentFile.parentFile.parentFile, // main
                files.platformFile,
                files.platformFile.parentFile, // package
                files.platformFile.parentFile.parentFile, // test
                files.platformFile.parentFile.parentFile.parentFile, // antibytes
                files.platformFile.parentFile.parentFile.parentFile.parentFile, // tech
                files.platformFile.parentFile.parentFile.parentFile.parentFile.parentFile, // kotlin
                files.platformFile.parentFile.parentFile.parentFile.parentFile.parentFile.parentFile, // main
            )
        )
    }
}
