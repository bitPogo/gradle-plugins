/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import com.appmattus.kotlinfixture.kotlinFixture
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
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility

/*
 * DO NOT MOVE THIS FILE
 *
 * Even if it seems appropriate to move it into `apple`, this leads to a bug where mockk will not resolve the static mock.
 */
class DefaultAppleDeviceResolverSpec {
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
    fun `Given ensureAppleDeviceCompatibility is called in non MacOsX Environment it does nothing`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns false

        // When
        kmpExtension.ensureAppleDeviceCompatibility()
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and the OS version is less then 12 6 it does nothing`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val targets: NamedDomainObjectCollection<KotlinTarget> = mockk()
        val os: OperatingSystem = mockk()
        val actualTargets = mutableListOf<KotlinNativeTargetWithSimulatorTests>(
            mockk(),
            mockk(),
        )

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns true
        every { os.version } returns "12.5"

        every { kmpExtension.targets } returns targets
        every {
            targets.withType(KotlinNativeTargetWithSimulatorTests::class.java).iterator()
        } returns actualTargets.listIterator()
        every { actualTargets[0].name } returns "ios"
        every { actualTargets[1].name } returns "watchos"

        // When
        kmpExtension.ensureAppleDeviceCompatibility()
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and the iOS version is greater equal then 12 6 it sets a new default device`() {
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
        every { actualTargets[0].name } returns "ios"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility()

        // Then
        verify(exactly = 1) {
            testRun.deviceId = "iPhone 14"
        }
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and the WatchOS version is greater equal then 12 6 it sets a new default device`() {
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
        every { actualTargets[0].name } returns "watchos"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility()

        // Then
        verify(exactly = 1) {
            testRun.deviceId = "Apple Watch Series 7 (45mm)"
        }
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and the iOS version is greater equal then 14 0 it sets a new default device`() {
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
        every { os.version } returns "14.0"

        every { kmpExtension.targets } returns targets
        every {
            targets.withType(KotlinNativeTargetWithSimulatorTests::class.java).iterator()
        } returns actualTargets.listIterator()
        every { actualTargets[0].name } returns "ios"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility()

        // Then
        verify(exactly = 1) {
            testRun.deviceId = "iPhone 15"
        }
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and the WatchOS version is greater equal then 14 0 it sets a new default device`() {
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
        every { actualTargets[0].name } returns "watchos"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility()

        // Then
        verify(exactly = 1) {
            testRun.deviceId = "Apple Watch Series 7 (45mm)"
        }
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and a customIos device is ignores pure blanks`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()
        val device = "    "
        val targets: NamedDomainObjectCollection<KotlinTarget> = mockk()
        val actualTargets = mutableListOf<KotlinNativeTargetWithSimulatorTests>(
            mockk(),
        )
        val testRuns: NamedDomainObjectContainer<KotlinNativeSimulatorTestRun> = mockk()
        val testRun: KotlinNativeSimulatorTestRun = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns true
        every { os.version } returns "11.2"

        every { kmpExtension.targets } returns targets
        every {
            targets.withType(KotlinNativeTargetWithSimulatorTests::class.java).iterator()
        } returns actualTargets.listIterator()
        every { actualTargets[0].name } returns "ios"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility(device)

        // Then
        verify(exactly = 0) {
            testRun.deviceId = any()
        }
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment less then 12 6 and a customIos device is set it sets a new default device`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()
        val device: String = fixture()
        val targets: NamedDomainObjectCollection<KotlinTarget> = mockk()
        val actualTargets = mutableListOf<KotlinNativeTargetWithSimulatorTests>(
            mockk(),
        )
        val testRuns: NamedDomainObjectContainer<KotlinNativeSimulatorTestRun> = mockk()
        val testRun: KotlinNativeSimulatorTestRun = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns true
        every { os.version } returns "11.2"

        every { kmpExtension.targets } returns targets
        every {
            targets.withType(KotlinNativeTargetWithSimulatorTests::class.java).iterator()
        } returns actualTargets.listIterator()
        every { actualTargets[0].name } returns "ios"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility(device)

        // Then
        verify(exactly = 1) {
            testRun.deviceId = device
        }
    }

    @Test
    fun `Given ensureAppleDeviceCompatibility is called in a MacOsX Environment and a customIos device is set it sets a new default device`() {
        // Given
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val os: OperatingSystem = mockk()
        val device: String = fixture()
        val targets: NamedDomainObjectCollection<KotlinTarget> = mockk()
        val actualTargets = mutableListOf<KotlinNativeTargetWithSimulatorTests>(
            mockk(),
        )
        val testRuns: NamedDomainObjectContainer<KotlinNativeSimulatorTestRun> = mockk()
        val testRun: KotlinNativeSimulatorTestRun = mockk()

        every { OperatingSystem.current() } returns os
        every { os.isMacOsX } returns true
        every { os.version } returns "13.2"

        every { kmpExtension.targets } returns targets
        every {
            targets.withType(KotlinNativeTargetWithSimulatorTests::class.java).iterator()
        } returns actualTargets.listIterator()
        every { actualTargets[0].name } returns "ios"
        every { actualTargets[0].testRuns } returns testRuns
        every { testRuns.getByName("test") } returns testRun
        every { testRun.deviceId = any() } just Runs

        // When
        kmpExtension.ensureAppleDeviceCompatibility(device)

        // Then
        verify(exactly = 1) {
            testRun.deviceId = device
        }
    }
}
