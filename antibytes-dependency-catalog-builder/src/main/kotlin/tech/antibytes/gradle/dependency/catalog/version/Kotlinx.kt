/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Kotlinx {
    /**
     * (AtomicFu)(https://github.com/Kotlin/kotlinx.atomicfu)
     */
    val atomicfu = AtomicFu

    internal object AtomicFu {
        private const val version = GradleVersions.kotlinxAtomicfu

        const val core = version
        const val gradle = version
        const val plugin = version
    }

    /**
     * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
     */
    val coroutines = Coroutines

    object Coroutines {
        private const val version = GradleVersions.kotlinxCoroutines

        const val bom = version
        const val core = version
        const val android = version
        const val javafx = version
        const val jdk8 = version
        const val jdk9 = version
        const val test = version
    }

    /**
     * [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
     */
    val serialization = Serialization

    object Serialization {
        private const val version = GradleVersions.kotlinxSerialization

        const val bom = version
        const val core = version
        const val cbor = version
        const val json = version
        const val jsonOkio = version
        const val hocon = version
        const val properties = version
        const val protobuf = version
        const val gradle = Kotlin.language
        const val plugin = Kotlin.language
    }

    /**
     * [Kotlin DateTime](https://github.com/Kotlin/kotlinx-datetime)
     */
    const val dateTime = GradleVersions.kotlinxDateTime

    /**
     * [KotlinNodeJS](https://github.com/Kotlin/kotlinx-nodejs)
     */
    const val nodeJs = GradleVersions.kotlinxNodeJs

    /**
     * [Kotlinx Wrappers](https://github.com/JetBrains/kotlin-wrappers/releases)
     */
    val wrappers = Wrappers

    object Wrappers {
        const val actionsKit = GradleVersions.kotlinxWrappersActionsKit
        const val bom = GradleVersions.kotlinxWrappersBom
        const val web = GradleVersions.kotlinxWrappersWeb
        const val typescript = GradleVersions.kotlinxWrappersTypescript
        const val popper = GradleVersions.kotlinxWrappersPopper
        const val node = GradleVersions.kotlinxWrappersNode
        const val js = GradleVersions.kotlinxWrappersJs
        const val extensions = GradleVersions.kotlinxWrappersExtensions
        const val emotion = GradleVersions.kotlinxWrappersEmotion
        const val cssType = GradleVersions.kotlinxWrappersCssType
        const val css = GradleVersions.kotlinxWrappersCss
        const val cesium = GradleVersions.kotlinxWrappersCesium
        const val browser = GradleVersions.kotlinxWrappersBrowser

        val mui = Mui

        object Mui {
            const val base = GradleVersions.kotlinxWrappersMuiBase
            const val material = GradleVersions.kotlinxWrappersMuiMaterial
            const val icons = GradleVersions.kotlinxWrappersMuiIcons
            const val lab = GradleVersions.kotlinxWrappersMuiLab
            const val system = GradleVersions.kotlinxWrappersMuiSystem
            const val dataPicker = GradleVersions.kotlinxWrappersMuiDataPicker
            const val treeView = GradleVersions.kotlinxWrappersMuiTreeView
        }

        val tanstack = Tanstack

        object Tanstack {
            const val virtualCore = GradleVersions.kotlinxWrappersTanstackVirtualCore
            const val queryCore = GradleVersions.kotlinxWrappersTanstackQueryCore
            const val tableCore = GradleVersions.kotlinxWrappersTanstackTableCore

            val react = React

            object React {
                const val query = GradleVersions.kotlinxWrappersTanstackReactQuery
                const val queryDevtools = GradleVersions.kotlinxWrappersTanstackReactQueryDevtools
                const val table = GradleVersions.kotlinxWrappersTanstackReactTable
                const val virtual = GradleVersions.kotlinxWrappersTanstackReactVirtual
            }
        }

        const val styledNext = GradleVersions.kotlinxWrappersStyledNext
        const val ringUi = GradleVersions.kotlinxWrappersRingUi
        const val remixRunRouter = GradleVersions.kotlinxWrappersRemixRunRouter
        const val redux = GradleVersions.kotlinxWrappersRedux

        val react = React

        object React {
            const val main = GradleVersions.kotlinxWrappersReactMain
            const val beautifulDnD = GradleVersions.kotlinxWrappersReactBeautifulDnD
            const val core = GradleVersions.kotlinxWrappersReactCore
            const val dom = GradleVersions.kotlinxWrappersReactDom
            const val legacyDom = GradleVersions.kotlinxWrappersReactLegacyDom
            const val domTestUtils = GradleVersions.kotlinxWrappersReactDomTestUtils
            const val legacy = GradleVersions.kotlinxWrappersReactLegacy
            const val redux = GradleVersions.kotlinxWrappersReactRedux
            const val router = GradleVersions.kotlinxWrappersReactRouter
            const val routerDom = GradleVersions.kotlinxWrappersReactRouterDom
            const val popper = GradleVersions.kotlinxWrappersReactPopper
            const val select = GradleVersions.kotlinxWrappersReactSelect
            const val use = GradleVersions.kotlinxWrappersReactUse
        }
    }
}
