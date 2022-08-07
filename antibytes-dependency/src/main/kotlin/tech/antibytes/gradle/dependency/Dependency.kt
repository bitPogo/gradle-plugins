/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
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

        object Square {

            val okio = OkIO

            object OkIO {
                const val common = "com.squareup.okio:okio:${Version.square.okio}"
                const val jvm = "com.squareup.okio:okio-jvm:${Version.square.okio}"
            }

            val sqldelight = SQLDelight

            object SQLDelight {
                const val gradle = "com.squareup.sqldelight:gradle-plugin:${Version.square.sqldelight}"
                const val android = "com.squareup.sqldelight:android-driver:${Version.square.sqldelight}"
                const val jvm = "com.squareup.sqldelight:sqlite-driver:${Version.square.sqldelight}"
                const val jvmJdbc = "com.squareup.sqldelight:jdbc-driver:${Version.square.sqldelight}"
                const val js = "com.squareup.sqldelight:sqljs-driver:${Version.square.sqldelight}"
                const val native = "com.squareup.sqldelight:native-driver:${Version.square.sqldelight}"
                const val coroutines = "com.squareup.sqldelight:coroutines-extensions:${Version.square.sqldelight}"
            }

            val okhttp = OkHttp

            object OkHttp {
                const val client = "com.squareup.okhttp3:okhttp:${Version.square.okhttp}"
                const val bom = "com.squareup.okhttp3:okhttp-bom:${Version.square.okhttp}"
                const val versionless = "com.squareup.okhttp3:okhttp"
                const val logger = "com.squareup.okhttp3:logging-interceptor"
                const val mockServer = "com.squareup.okhttp3:mockwebserver:${Version.square.okhttp}"
            }
        }

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

    val jvm = Jvm

    object Jvm {
        val test = Test

        object Test {
            const val kotlin = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin.stdlib}"
            const val junit = "org.junit:junit-bom:${Version.jvm.test.junit}"
            const val jupiter = "org.junit.jupiter:junit-jupiter"

            const val mockk = "io.mockk:mockk:${Version.kotlin.test.mockk}"
            const val koin = "io.insert-koin:koin-test:${Version.kotlin.koin}"
        }
    }

    val android = Android

    object Android {
        // AGP
        const val androidGradlePlugin = "com.android.tools.build:gradle:${Version.android.androidGradlePlugin}"

        // Android
        const val desugar = "com.android.tools:desugar_jdk_libs:${Version.android.desugar}"

        // AndroidX
        val ktx = Ktx

        object Ktx {
            const val annotation = "androidx.annotation:annotation:${Version.android.ktx.annotation}"
            const val core = "androidx.core:core-ktx:${Version.android.ktx.core}"
            const val activity = "androidx.activity:activity-ktx:${Version.android.ktx.activity}"
            const val collections = "androidx.collection:collection-ktx:${Version.android.ktx.collections}"
            const val fragment = "androidx.fragment:fragment-ktx:${Version.android.ktx.fragment}"
            const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.android.ktx.lifecycle}"
            const val navigationRuntime = "androidx.navigation:navigation-runtime-ktx:${Version.android.ktx.navigation}"
            const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Version.android.ktx.navigation}"
            const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.android.ktx.navigation}"
            const val palette = "androidx.palette:palette-ktx:${Version.android.ktx.palette}"
            const val paging = "androidx.paging:paging-runtime:${Version.android.ktx.pageing}"
            const val runtime = "androidx.compose.runtime:runtime:${Version.android.compose.runtime}"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.android.ktx.viewmodel}"
            const val viewmodelSaver = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.android.ktx.viewmodel}"
            const val viewmodelCoroutine = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.android.ktx.viewmodel}"
            const val workmanager = "androidx.work:work-runtime-ktx:${Version.android.ktx.workmanager}"
        }

        val appCompact = AppCompact

        object AppCompact {
            const val core = "androidx.appcompat:appcompat:${Version.android.appCompat}"
            const val resources = "androidx.appcompat:appcompat-resources:${Version.android.appCompat}"
        }

        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.android.constraintLayout}"

        // Material
        const val material = "com.google.android.material:material:${Version.android.material}"

        val compose = Compose

        // see: https://developer.android.com/jetpack/compose/setup
        object Compose {
            const val ui = "androidx.compose.ui:ui:${Version.android.compose.ui}"
            const val uiTooling = "androidx.compose.ui:ui-tooling:${Version.android.compose.ui}"
            const val uiManifest = "androidx.compose.ui:ui-test-manifest:${Version.android.compose.ui}"
            const val foundation = "androidx.compose.foundation:foundation:${Version.android.compose.foundation}"
            const val navigation = "androidx.navigation:navigation-compose:${Version.android.ktx.navigation}"
            const val material = "androidx.compose.material:material:${Version.android.compose.material}"
            const val materialIcons = "androidx.compose.material:material-icons-core:${Version.android.compose.material}"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${Version.android.compose.material}"
            const val activity = "androidx.activity:activity-compose:${Version.android.compose.activity}"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.android.compose.viewmodel}"
            const val constrainLayout = "androidx.constraintlayout:constraintlayout-compose:${Version.android.compose.constraintLayout}"
            const val paging = "androidx.paging:paging-compose:${Version.android.compose.paging}"
        }

        val test = Test

        object Test {
            const val core = "androidx.test:core:${Version.android.test.test}"
            const val ktx = "androidx.test:core-ktx:${Version.android.test.test}"
            const val runner = "androidx.test:runner:${Version.android.test.test}"
            const val rules = "androidx.test:rules:${Version.android.test.test}"

            const val junit = "androidx.test.ext:junit:${Version.android.test.junit}"
            const val junitKtx = "androidx.test.ext:junit-ktx:${Version.android.test.junit}"

            const val junit5 = "org.junit.jupiter:junit-jupiter-api:${Version.jvm.test.junit}"
            const val junit5Parameterized = "org.junit.jupiter:junit-jupiter-params:${Version.jvm.test.junit}"

            const val junit4 = "junit:junit:${Version.jvm.test.junit4}"
            const val junit4LegacyEngine = "org.junit.vintage:junit-vintage-engine:${Version.jvm.test.junit}"

            const val espressoCore = "androidx.test.espresso:espresso-core:${Version.android.test.espresso}"
            const val espressoIntents = "androidx.test.espresso:espresso-intents:${Version.android.test.espresso}"
            const val espressoWeb = "androidx.test.espresso:espresso-web:${Version.android.test.espresso}"

            const val composeJunit4 = "androidx.compose.ui:ui-test-junit4:${Version.android.compose.ui}"

            const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Version.android.test.uiAutomator}"

            const val robolectric = "org.robolectric:robolectric:${Version.android.test.robolectric}"

            const val paging = "androidx.paging:paging-common:${Version.android.ktx.pageing}"
        }
    }

    object Hilt {
        const val gradle = "com.google.dagger:hilt-android-gradle-plugin:${Version.google.hilt}"
        const val core = "com.google.dagger:hilt-android:${Version.google.hilt}"
        const val compiler = "com.google.dagger:hilt-compiler:${Version.google.hilt}"
        const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Version.google.hiltCompose}"
        const val test = "com.google.dagger:hilt-android-testing:${Version.google.hilt}"
    }

    val js = JS

    object JS {
        const val nodejs = "org.jetbrains.kotlinx:kotlinx-nodejs:${Version.js.nodeJs}"

        val test = Test

        object Test {
            const val js = "org.jetbrains.kotlin:kotlin-test-js:${Version.kotlin.stdlib}"
        }
    }
}
