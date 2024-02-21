/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.android

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJavaToolchain
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.configuration.android.config.MainConfig
import tech.antibytes.gradle.test.invokeGradleAction

class ToolChainConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given configure is called ignores all explict Jvm ToolChains`() {
        // Given
        val jvmTask: KotlinCompile = mockk {
            every { name } returns "${fixture<String>()}KotlinJvm"
        }
        val taskContainer: TaskContainer = mockk()
        val project: Project = mockk {
            every { tasks } returns taskContainer
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainConfigurator.configure(project)

        // Then
        verify(exactly = 2) { jvmTask.name }
        confirmVerified(jvmTask)
    }

    @Test
    fun `Given configure is called ignores all implicit Jvm ToolChains`() {
        // Given
        val jvmTask: KotlinCompile = mockk {
            every { name } returns fixture<String>()
        }
        val taskContainer: TaskContainer = mockk()
        val project: Project = mockk {
            every { tasks } returns taskContainer
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainConfigurator.configure(project)

        // Then
        verify(exactly = 2) { jvmTask.name }
        confirmVerified(jvmTask)
    }

    @Test
    fun `Given configure is called configures all plain Jvm ToolChains`() {
        // Given
        val toolChain: KotlinJavaToolchain.JavaToolchainSetter = mockk(relaxed = true)
        val provider: Provider<JavaLauncher> = mockk()
        val jvmTask: KotlinCompile = mockk(relaxed = true) {
            every { name } returns "${fixture<String>()}Kotlin"
            every { kotlinJavaToolchain.toolchain } returns toolChain
        }
        val taskContainer: TaskContainer = mockk()
        val toolchainConfiguration = slot<Action<in JavaToolchainSpec>>()
        val toolchainService: JavaToolchainService = mockk {
            every { launcherFor(capture(toolchainConfiguration)) } returns provider
        }
        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { extensions.getByType(JavaToolchainService::class.java) } returns toolchainService
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainConfigurator.configure(project)

        // Then
        verify(exactly = 1) { toolChain.use(provider) }
        val proof: JavaToolchainSpec = mockk(relaxed = true)
        toolchainConfiguration.captured.execute(proof)
        verify(exactly = 1) {
            proof.languageVersion.set(JavaLanguageVersion.of(MainConfig.javaVersion))
        }
    }

    @Test
    fun `Given configure is called configures all plain Android ToolChains`() {
        // Given
        val toolChain: KotlinJavaToolchain.JavaToolchainSetter = mockk(relaxed = true)
        val provider: Provider<JavaLauncher> = mockk()
        val jvmTask: KotlinCompile = mockk(relaxed = true) {
            every { name } returns "${fixture<String>()}KotlinAndroid"
            every { kotlinJavaToolchain.toolchain } returns toolChain
        }
        val taskContainer: TaskContainer = mockk()
        val toolchainConfiguration = slot<Action<in JavaToolchainSpec>>()
        val toolchainService: JavaToolchainService = mockk {
            every { launcherFor(capture(toolchainConfiguration)) } returns provider
        }
        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { extensions.getByType(JavaToolchainService::class.java) } returns toolchainService
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainConfigurator.configure(project)

        // Then
        verify(exactly = 1) { toolChain.use(provider) }
        val proof: JavaToolchainSpec = mockk(relaxed = true)
        toolchainConfiguration.captured.execute(proof)
        verify(exactly = 1) {
            proof.languageVersion.set(JavaLanguageVersion.of(MainConfig.javaVersion))
        }
    }

    @Test
    fun `It fulfils ParameterlessConfigurator`() {
        val configurator = ToolChainConfigurator as Any

        assertTrue { configurator is ConfigurationContract.ParameterlessConfigurator }
    }
}
