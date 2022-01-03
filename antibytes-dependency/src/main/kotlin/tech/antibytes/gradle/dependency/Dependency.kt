/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

object Dependency {

    val multiplatform = Multiplatform

    object Multiplatform {
        val kotlin = Kotlin

        object Kotlin {
            const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Version.kotlin.stdlib}"
            const val jdk = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
            const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin.stdlib}"
            const val js = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
            const val native = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
            const val android = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
        }

        val coroutines = Coroutines

        object Coroutines {
            const val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlin.coroutines}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlin.coroutines}"
            const val js = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Version.kotlin.coroutines}"
        }

        val koin = Koin

        object Koin {
            const val core = "io.insert-koin:koin-core:${Version.kotlin.koin}"
            const val android = "io.insert-koin:koin-android:${Version.kotlin.koin}"
            const val androidAppCompact = "io.insert-koin:koin-android-compat:${Version.kotlin.koin}"
            const val jetpackWorkmanager = "io.insert-koin:koin-androidx-workmanager:${Version.kotlin.koin}"
            const val jetpackNavigation = "io.insert-koin:koin-androidx-navigation:${Version.kotlin.koin}"
            const val jetpackCompose = "io.insert-koin:koin-androidx-compose:${Version.kotlin.koin}"
        }

        object Ktor {
            const val commonCore = "io.ktor:ktor-client-core:${Version.kotlin.ktor}"
            const val commonJson = "io.ktor:ktor-client-json:${Version.kotlin.ktor}"
            const val jvmCore = "io.ktor:ktor-client-core-jvm:${Version.kotlin.ktor}"
            const val androidCore = "io.ktor:ktor-client-android:${Version.kotlin.ktor}"
            const val jvmJson = "io.ktor:ktor-client-json-jvm:${Version.kotlin.ktor}"
            const val ios = "io.ktor:ktor-client-ios:${Version.kotlin.ktor}"
            const val iosCore = "io.ktor:ktor-client-core:${Version.kotlin.ktor}"
            const val iosJson = "io.ktor:ktor-client-json-native:${Version.kotlin.ktor}"
            const val commonSerialization = "io.ktor:ktor-client-serialization:${Version.kotlin.ktor}"
            const val androidSerialization = "io.ktor:ktor-client-serialization-jvm:${Version.kotlin.ktor}"
            const val iosSerialization = "io.ktor:ktor-client-serialization-native:${Version.kotlin.ktor}"

            // Logger
            const val logger = "io.ktor:ktor-client-logging:${Version.kotlin.ktor}"

            // Testing
            const val mock = "io.ktor:ktor-client-mock:${Version.kotlin.ktor}"
            const val jvmMock = "io.ktor:ktor-client-mock-jvm:${Version.kotlin.ktor}"
            const val nativeMock = "io.ktor:ktor-client-mock-native:${Version.kotlin.ktor}"
        }

        val serialization = Serialization

        object Serialization {
            const val common =
                "org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.kotlin.serialization}"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${Version.kotlin.serialization}"
            const val protobuf =
                "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${Version.kotlin.serialization}"
        }

        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.kotlin.dateTime}"

        const val uuid = "com.benasher44:uuid:${Version.kotlin.uuid}"

        val test = Test

        object Test {
            const val common = "org.jetbrains.kotlin:kotlin-test-common:${Version.kotlin.stdlib}"
            const val annotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Version.kotlin.stdlib}"
            const val jvm = "org.jetbrains.kotlin:kotlin-test:${Version.kotlin.stdlib}"
            const val junit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin.stdlib}"

            val mockk = Mockk

            object Mockk {
                const val junit = "io.mockk:mockk:${Version.kotlinTest.mockk}"
            }

            const val fixture = "com.appmattus.fixture:fixture:${Version.kotlinTest.fixture}"
        }

        val koinTest = KoinTest

        object KoinTest {
            val koinTest = "io.insert-koin:koin-test:${Version.kotlin.koin}"
        }
    }

    val jvmTest = JvmTest

    object JvmTest {
        const val junit = "org.junit:junit-bom:${Version.jvmTest.junit}"
        const val jupiter = "org.junit.jupiter:junit-jupiter"
    }

    val android = Android

    object Android {
        // Android
        const val desugar = "com.android.tools:desugar_jdk_libs:${Version.android.desugar}"

        // AndroidX
        const val ktx = "androidx.core:core-ktx:${Version.android.ktx}"
        const val appCompat = "androidx.appcompat:appcompat:${Version.android.appCompat}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.android.constraintLayout}"
        const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:${Version.android.constraintLayoutCompose}"

        // Material
        const val material = "com.google.android.material:material:${Version.android.material}"
    }

    val androidTest = AndroidTest

    object AndroidTest {
        const val core = "androidx.test:core:${Version.androidTest.test}"
        const val runner = "androidx.test:runner:${Version.androidTest.test}"
        const val rules = "androidx.test:rules:${Version.androidTest.test}"

        const val junit = "androidx.test.ext:junit:${Version.androidTest.test}"

        const val junit5 = "org.junit.jupiter:junit-jupiter-api:${Version.jvmTest.junit}"
        const val junit5Parameterized = "org.junit.jupiter:junit-jupiter-params:${Version.jvmTest.junit}"

        const val junit4 = "junit:junit:${Version.jvmTest.junit4}"
        const val junit4LegacyEngine = "org.junit.vintage:junit-vintage-engine:${Version.jvmTest.junit}"

        const val espressoCore = "androidx.test.espresso:espresso-core:${Version.androidTest.espresso}"
        const val espressoIntents = "androidx.test.espresso:espresso-intents:${Version.androidTest.espresso}"
        const val espressoWeb = "androidx.test.espresso:espresso-web:${Version.androidTest.espresso}"

        const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Version.androidTest.uiAutomator}"

        const val robolectric = "org.robolectric:robolectric:${Version.androidTest.robolectric}"
    }
}
