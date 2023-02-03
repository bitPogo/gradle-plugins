/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.kotlin

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Wrappers {
    private const val group = "org.jetbrains.kotlin-wrappers"

    val browser = MavenArtifact(
        group = group,
        id = "kotlin-browser",
        platform = Platform.JS
    )
    val cesium = MavenArtifact(
        group = group,
        id = "kotlin-cesium",
        platform = Platform.JS
    )
    val css = MavenArtifact(
        group = group,
        id = "kotlin-css",
        platform = Platform.JS
    )
    val cssType = MavenArtifact(
        group = group,
        id = "kotlin-csstype",
        platform = Platform.JS
    )
    val emotion = MavenArtifact(
        group = group,
        id = "kotlin-emotion",
        platform = Platform.JS
    )
    val extensions = MavenArtifact(
        group = group,
        id = "kotlin-extensions",
        platform = Platform.JS
    )
    val history = MavenArtifact(
        group = group,
        id = "kotlin-history",
        platform = Platform.JS
    )
    val js = MavenArtifact(
        group = group,
        id = "kotlin-js",
        platform = Platform.JS
    )
    val mui = MavenArtifact(
        group = group,
        id = "kotlin-mui",
        platform = Platform.JS
    )
    val muiIcons = MavenArtifact(
        group = group,
        id = "kotlin-mui-icons",
        platform = Platform.JS
    )
    val node = MavenArtifact(
        group = group,
        id = "kotlin-node",
        platform = Platform.JS
    )
    val popper = MavenArtifact(
        group = group,
        id = "kotlin-popper",
        platform = Platform.JS
    )
    val bom = MavenArtifact(
        group = group,
        id = "kotlin-wrappers-bom",
        platform = Platform.JS
    )
    val web = MavenArtifact(
        group = group,
        id = "kotlin-web",
        platform = Platform.JS
    )
    val typescript = MavenArtifact(
        group = group,
        id = "kotlin-typescript",
        platform = Platform.JS
    )
    val styledNext = MavenArtifact(
        group = group,
        id = "kotlin-styled-next",
        platform = Platform.JS
    )
    val ringUi = MavenArtifact(
        group = group,
        id = "kotlin-ring-ui",
        platform = Platform.JS
    )
    val remixRunRouter = MavenArtifact(
        group = group,
        id = "kotlin-remix-run-router",
        platform = Platform.JS
    )
    val redux = MavenArtifact(
        group = group,
        id = "kotlin-redux",
        platform = Platform.JS
    )

    val react = React

    object React {
        val main = MavenArtifact(
            group = group,
            id = "kotlin-react",
            platform = Platform.JS
        )
        val beautifulDnD = MavenArtifact(
            group = group,
            id = "kotlin-react-beautiful-dnd",
            platform = Platform.JS
        )
        val core = MavenArtifact(
            group = group,
            id = "kotlin-react-core",
            platform = Platform.JS
        )
        val dom = MavenArtifact(
            group = group,
            id = "kotlin-react-dom",
            platform = Platform.JS
        )
        val legacyDom = MavenArtifact(
            group = group,
            id = "kotlin-react-dom-legacy",
            platform = Platform.JS
        )
        val domTestUtils = MavenTestArtifact(
            group = group,
            id = "kotlin-react-dom-test-utils",
            platform = Platform.JS
        )
        val legacy = MavenArtifact(
            group = group,
            id = "kotlin-react-legacy",
            platform = Platform.JS
        )
        val redux = MavenArtifact(
            group = group,
            id = "kotlin-react-redux",
            platform = Platform.JS
        )
        val router = MavenArtifact(
            group = group,
            id = "kotlin-react-router",
            platform = Platform.JS
        )
        val routerDom = MavenArtifact(
            group = group,
            id = "kotlin-react-router-dom",
            platform = Platform.JS
        )
        val popper = MavenArtifact(
            group = group,
            id = "kotlin-react-popper",
            platform = Platform.JS
        )
        val select = MavenArtifact(
            group = group,
            id = "kotlin-react-select",
            platform = Platform.JS
        )
        val use = MavenArtifact(
            group = group,
            id = "kotlin-react-use",
            platform = Platform.JS
        )
    }

    val tanstack = Tanstack

    object Tanstack {
        val tableCore  = MavenArtifact(
            group = group,
            id = "kotlin-tanstack-table-core",
            platform = Platform.JS
        )
        val virtualCore = MavenArtifact(
            group = group,
            id = "kotlin-tanstack-virtual-core",
            platform = Platform.JS
        )
        val queryCore = MavenArtifact(
            group = group,
            id = "kotlin-tanstack-query-core",
            platform = Platform.JS
        )

        val react = React

        object React {
            val query = MavenArtifact(
                group = group,
                id = "kotlin-tanstack-react-query",
                platform = Platform.JS
            )
            val queryDevtools = MavenArtifact(
                group = group,
                id = "kotlin-tanstack-react-query-devtools",
                platform = Platform.JS
            )
            val table = MavenArtifact(
                group = group,
                id = "kotlin-tanstack-react-table",
                platform = Platform.JS
            )
            val virtual = MavenArtifact(
                group = group,
                id = "kotlin-tanstack-react-virtual",
                platform = Platform.JS
            )
        }
    }
}
