/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages

import tech.antibytes.gradle.dependency.Version

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
