/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages

import tech.antibytes.gradle.dependency.Version

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

    val atomicFu = AtomicFu

    object AtomicFu {
        const val common = "org.jetbrains.kotlinx:atomicfu:${Version.kotlin.atomicFu}"
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

    val ktor = Ktor

    object Ktor {
        val common = Common

        object Common {
            const val client = "io.ktor:ktor-client-core:${Version.kotlin.ktor}"
            const val json = "io.ktor:ktor-serialization-kotlinx-json:${Version.kotlin.ktor}"
            const val xml = "io.ktor:ktor-serialization-kotlinx-xml:${Version.kotlin.ktor}"
            const val cbor = "io.ktor:ktor-serialization-kotlinx-cbor:${Version.kotlin.ktor}"
            const val websockets = "io.ktor:ktor-client-websockets:${Version.kotlin.ktor}"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Version.kotlin.ktor}"
        }

        val jvm = Jvm

        object Jvm {
            const val client = "io.ktor:ktor-client-java:${Version.kotlin.ktor}"
            const val jetty = "io.ktor:ktor-client-jetty:${Version.kotlin.ktor}"
            const val apache = "io.ktor:ktor-client-apache:${Version.kotlin.ktor}"
            const val cio = "io.ktor:ktor-client-cio:${Version.kotlin.ktor}"
            const val json = "io.ktor:ktor-serialization-kotlinx-json-jvm:${Version.kotlin.ktor}"
            const val xml = "io.ktor:ktor-serialization-kotlinx-xml-jvm:${Version.kotlin.ktor}"
            const val cbor = "io.ktor:ktor-serialization-kotlinx-cbor-jvm:${Version.kotlin.ktor}"
            const val websockets = "io.ktor:ktor-client-websockets-jvm:${Version.kotlin.ktor}"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation-jvm:${Version.kotlin.ktor}"
        }

        val android = Android

        object Android {
            const val client = "io.ktor:ktor-client-android:${Version.kotlin.ktor}"
            const val okhttp = "io.ktor:ktor-client-okhttp:${Version.kotlin.ktor}"
            const val json = "io.ktor:ktor-serialization-kotlinx-json-android:${Version.kotlin.ktor}"
            const val xml = "io.ktor:ktor-serialization-kotlinx-xml-android:${Version.kotlin.ktor}"
            const val cbor = "io.ktor:ktor-serialization-kotlinx-cbor-android:${Version.kotlin.ktor}"
            const val websockets = "io.ktor:ktor-client-websockets-android:${Version.kotlin.ktor}"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation-android:${Version.kotlin.ktor}"
        }

        val ios = Ios

        object Ios {
            const val client = "io.ktor:ktor-client-darwin:${Version.kotlin.ktor}"
            const val json = "io.ktor:ktor-serialization-kotlinx-json-native:${Version.kotlin.ktor}"
            const val xml = "io.ktor:ktor-serialization-kotlinx-xml-native:${Version.kotlin.ktor}"
            const val cbor = "io.ktor:ktor-serialization-kotlinx-cbor-native:${Version.kotlin.ktor}"
            const val websockets = "io.ktor:ktor-client-websockets-native:${Version.kotlin.ktor}"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation-native:${Version.kotlin.ktor}"
        }

        val js = JS

        object JS {
            const val client = "io.ktor:ktor-client-js:${Version.kotlin.ktor}"
            const val serialization = "io.ktor:ktor-client-serialization-js:${Version.kotlin.ktor}"
            const val json = "io.ktor:ktor-serialization-kotlinx-json-js:${Version.kotlin.ktor}"
            const val xml = "io.ktor:ktor-serialization-kotlinx-xml-js:${Version.kotlin.ktor}"
            const val cbor = "io.ktor:ktor-serialization-kotlinx-cbor-js:${Version.kotlin.ktor}"
            const val websockets = "io.ktor:ktor-client-websockets-js:${Version.kotlin.ktor}"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation-js:${Version.kotlin.ktor}"
        }

        val native = Native

        object Native {
            const val client = "io.ktor:ktor-client-curl:${Version.kotlin.ktor}"
            const val json = "io.ktor:ktor-serialization-kotlinx-json-native:${Version.kotlin.ktor}"
            const val xml = "io.ktor:ktor-serialization-kotlinx-xml-native:${Version.kotlin.ktor}"
            const val cbor = "io.ktor:ktor-serialization-kotlinx-cbor-native:${Version.kotlin.ktor}"
            const val websockets = "io.ktor:ktor-client-websockets-native:${Version.kotlin.ktor}"
            const val contentNegotiation = "io.ktor:ktor-client-content-negotiation-native:${Version.kotlin.ktor}"
        }

        // Logger
        const val logger = "io.ktor:ktor-client-logging:${Version.kotlin.ktor}"

        // Testing
        const val mock = "io.ktor:ktor-client-mock:${Version.kotlin.ktor}"
    }

    val serialization = Serialization

    object Serialization {
        const val common = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.kotlin.serialization}"
        const val android = "org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${Version.kotlin.serialization}"
        const val protobuf = "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${Version.kotlin.serialization}"
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlin.serialization}"
    }

    const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.kotlin.dateTime}"

    const val uuid = "com.benasher44:uuid:${Version.kotlin.uuid}"

    val stately = Stately

    object Stately {
        const val isolate = "co.touchlab:stately-isolate:${Version.kotlin.stately}"
        const val freeze = "co.touchlab:stately-common:${Version.kotlin.stately}"
        const val concurrency = "co.touchlab:stately-concurrency:${Version.kotlin.stately}"
        const val collections = "co.touchlab:stately-iso-collections:${Version.kotlin.stately}"
    }

    val square = Square

    val test = Test

    object Test {
        const val common = "org.jetbrains.kotlin:kotlin-test-common:${Version.kotlin.stdlib}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.kotlin.coroutines}"
        const val annotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Version.kotlin.stdlib}"
        const val js = "org.jetbrains.kotlin:kotlin-test-js:${Version.kotlin.stdlib}"
        const val jvm = "org.jetbrains.kotlin:kotlin-test:${Version.kotlin.stdlib}"
        const val junit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin.stdlib}"
    }
}
