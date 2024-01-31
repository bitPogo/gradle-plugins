/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality.stableapi

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.AntibytesQualityExtension
import tech.antibytes.gradle.quality.QualityContract.Configurator
import tech.antibytes.gradle.quality.api.StableApiConfiguration
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction

class StableApiSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = StableApi

        assertTrue(configurator is Configurator)
    }

    @Test
    fun `Given configure is called it does nothing if no stable api configuration was given`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()
        extension.stableApi.convention(null)

        // When
        StableApi.configure(project, extension)
    }

    @Test
    fun `Given configure is called it configures the StableApi extension was given`() {
        // Given
        val project: Project = mockk()
        val config = StableApiConfiguration(
            excludeProjects = fixture(),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val stableApi: ApiValidationExtension = mockk(relaxed = true)

        val excludeProjects = slot<Set<String>>()
        val excludePackages = slot<Set<String>>()
        val excludeClasses = slot<Set<String>>()
        val nonPublicMarkers = slot<Set<String>>()

        extension.stableApi.convention(config)

        every { stableApi.ignoredProjects.addAll(capture(excludeProjects)) } returns fixture()
        every { stableApi.ignoredPackages.addAll(capture(excludePackages)) } returns fixture()
        every { stableApi.ignoredClasses.addAll(capture(excludeClasses)) } returns fixture()
        every { stableApi.nonPublicMarkers.addAll(capture(nonPublicMarkers)) } returns fixture()

        invokeGradleAction(
            stableApi,
            stableApi,
        ) { probe ->
            project.extensions.configure(ApiValidationExtension::class.java, probe)
        }

        every { project.subprojects } returns mockk(relaxed = true)
        every { project.plugins.apply(any()) } returns mockk()

        // When
        StableApi.configure(project, extension)

        // Then
        assertEquals(
            actual = excludeProjects.captured,
            expected = config.excludeProjects,
        )
        assertEquals(
            actual = excludePackages.captured,
            expected = config.excludePackages,
        )
        assertEquals(
            actual = excludeClasses.captured,
            expected = config.excludeClasses,
        )
        assertEquals(
            actual = nonPublicMarkers.captured,
            expected = config.nonPublicMarkers,
        )

        verify(exactly = 1) { project.plugins.apply("org.jetbrains.kotlinx.binary-compatibility-validator") }
    }

    @Test
    fun `Given configure is called it enables explicit Api for KotlinAndroid`() {
        // Given
        val project: Project = mockk()
        val config = StableApiConfiguration(
            excludeProjects = fixture(),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinAndroid: KotlinAndroidProjectExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns kotlinAndroid
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns null
        every { subproject.name } returns fixture()

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 1) { kotlinAndroid.explicitApi() }
    }

    @Test
    fun `Given configure is called it ignores explicit Api for KotlinAndroid if the project is ignored`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val config = StableApiConfiguration(
            excludeProjects = setOf(subprojectName),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinAndroid: KotlinAndroidProjectExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns kotlinAndroid
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns null
        every { subproject.name } returns subprojectName

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 0) { kotlinAndroid.explicitApi() }
    }

    @Test
    fun `Given configure is called it enables explicit Api for KotlinJvm`() {
        // Given
        val project: Project = mockk()
        val config = StableApiConfiguration(
            excludeProjects = fixture(),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinJvm: KotlinJvmProjectExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns kotlinJvm
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns null
        every { subproject.name } returns fixture()

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 1) { kotlinJvm.explicitApi() }
    }

    @Test
    fun `Given configure is called it ignores explicit Api for KotlinJvm if the project is ignored`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val config = StableApiConfiguration(
            excludeProjects = setOf(subprojectName),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinJvm: KotlinJvmProjectExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns kotlinJvm
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns null
        every { subproject.name } returns subprojectName

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 0) { kotlinJvm.explicitApi() }
    }

    @Test
    fun `Given configure is called it enables explicit Api for KotlinJs`() {
        // Given
        val project: Project = mockk()
        val config = StableApiConfiguration(
            excludeProjects = fixture(),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinJs: KotlinJsProjectExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns kotlinJs
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns null
        every { subproject.name } returns fixture()

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 1) { kotlinJs.explicitApi() }
    }

    @Test
    fun `Given configure is called it ignores explicit Api for KotlinJs if the project is ignored`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val config = StableApiConfiguration(
            excludeProjects = setOf(subprojectName),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinJs: KotlinJsProjectExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns kotlinJs
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns null
        every { subproject.name } returns subprojectName

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 0) { kotlinJs.explicitApi() }
    }

    @Test
    fun `Given configure is called it enables explicit Api for KotlinMultiplatfom`() {
        // Given
        val project: Project = mockk()
        val config = StableApiConfiguration(
            excludeProjects = fixture(),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinMP: KotlinMultiplatformExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns kotlinMP
        every { subproject.name } returns fixture()

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 1) { kotlinMP.explicitApi() }
    }

    @Test
    fun `Given configure is called it ignores explicit Api for KotlinMultiplatform if the project is ignored`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val config = StableApiConfiguration(
            excludeProjects = setOf(subprojectName),
            excludePackages = fixture(),
            excludeClasses = fixture(),
            nonPublicMarkers = fixture(),
        )
        val extension = createExtension<AntibytesQualityExtension>()

        val subproject: Project = mockk()
        val subprojects: Set<Project> = setOf(subproject)

        val kotlinMP: KotlinMultiplatformExtension = mockk(relaxed = true)

        extension.stableApi.convention(config)

        every { project.plugins.apply(any()) } returns mockk()
        every {
            project.extensions.configure(ApiValidationExtension::class.java, any())
        } returns mockk()
        every { project.subprojects } returns subprojects
        every { subproject.extensions.findByType(KotlinAndroidProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJvmProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinJsProjectExtension::class.java) } returns null
        every { subproject.extensions.findByType(KotlinMultiplatformExtension::class.java) } returns kotlinMP
        every { subproject.name } returns subprojectName

        // When
        StableApi.configure(project, extension)

        // Then
        verify(exactly = 0) { kotlinMP.explicitApi() }
    }
}
