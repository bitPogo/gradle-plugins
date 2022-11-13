/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.Platform

internal object Compose {
    private const val group = "androidx.compose"

    val bom = MavenArtifact(
        group = group,
        id = "compose-bom",
        platform = Platform.ANDROID,
    )
    val runtime = MavenArtifact(
        group = "$group.runtime",
        id = "runtime",
        platform = Platform.ANDROID,
    )
    val saveable = MavenArtifact(
        group = "$group.runtime",
        id = "runtime-saveable",
        platform = Platform.ANDROID,
    )

    val animation = Animation

    internal object Animation {
        private const val group = "${Compose.group}.animation"
        val core = MavenArtifact(
            group = group,
            id = "animation",
            platform = Platform.ANDROID,
        )
        val runtime = MavenArtifact(
            group = group,
            id = "animation-core",
            platform = Platform.ANDROID,
        )
        val graphics = MavenArtifact(
            group = group,
            id = "animation-graphics",
            platform = Platform.ANDROID,
        )
    }

    val foundation = Foundation

    internal object Foundation {
        private const val group = "${Compose.group}.foundation"
        val core = MavenArtifact(
            group = group,
            id = "foundation",
            platform = Platform.ANDROID,
        )
        val layout = MavenArtifact(
            group = group,
            id = "foundation-layout",
            platform = Platform.ANDROID,
        )
    }

    val ui = UI

    internal object UI {
        private const val group = "${Compose.group}.ui"
        val core = MavenArtifact(
            group = group,
            id = "ui",
            platform = Platform.ANDROID,
        )
        val geometry = MavenArtifact(
            group = group,
            id = "ui-geometry",
            platform = Platform.ANDROID,
        )
        val graphics = MavenArtifact(
            group = group,
            id = "ui-graphics",
            platform = Platform.ANDROID,
        )
        val text = MavenArtifact(
            group = group,
            id = "ui-text",
            platform = Platform.ANDROID,
        )
        val util = MavenArtifact(
            group = group,
            id = "ui-util",
            platform = Platform.ANDROID,
        )
        val unit = MavenArtifact(
            group = group,
            id = "ui-unit",
            platform = Platform.ANDROID,
        )

        val tooling = Tooling

        internal object Tooling {
            val core = MavenArtifact(
                group = group,
                id = "ui-tooling",
                platform = Platform.ANDROID,
            )
            val data = MavenArtifact(
                group = group,
                id = "ui-tooling-data",
                platform = Platform.ANDROID,
            )
            val preview = MavenArtifact(
                group = group,
                id = "ui-tooling-preview",
                platform = Platform.ANDROID,
            )
        }
    }
}
