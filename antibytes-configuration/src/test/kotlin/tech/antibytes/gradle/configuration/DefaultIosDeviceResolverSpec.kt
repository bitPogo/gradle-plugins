/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeSimulatorTestRun
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultIosDeviceResolverSpec {
    @BeforeEach
    fun setUp() {
        mockkStatic(OperatingSystem::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(OperatingSystem::class)
    }

    @Test
    fun `Given ensureIosDeviceCompatibility is called in non MacOsX Environment it does nothing`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns false

        // When
        kmpExtension.ensureIosDeviceCompatibility()
    }

    @Test
    fun `Given ensureIosDeviceCompatibility is called in a MacOsX Environment and the OS version is less then 12 6 it does nothing`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns true
        every { os.version } returns "12.5"

        // When
        kmpExtension.ensureIosDeviceCompatibility()
    }

    @Test
    fun `Given ensureIosDeviceCompatibility is called in a MacOsX Environment and the OS version is greater equal then 12 6 it sets a new default device`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()
        val targets: NamedDomainObjectCollection<KotlinTarget> = mockk()
        val actualTargets = mutableListOf<KotlinNativeTargetWithSimulatorTests>(
            mockk(),
        )
        val testRuns: NamedDomainObjectContainer<KotlinNativeSimulatorTestRun> = mockk()
        val testRun: KotlinNativeSimulatorTestRun = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns true
        every { os.version } returns "12.6"

        every { kmpExtension.targets } returns targets
        every {
            targets.withType(KotlinNativeTargetWithSimulatorTests::class.java).iterator()
        } returns actualTargets.listIterator()
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureIosDeviceCompatibility()

        // Then
        verify(exactly = 1) {
            testRun.deviceId = "iPhone 14"
        }
    }
}
