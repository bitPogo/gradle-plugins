/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.PythonArtifact

internal object MkDocs {
    val includeMarkdown = PythonArtifact(
        id = "mkdocs-include-markdown-plugin",
    )
    val kroki = PythonArtifact(
        id = "mkdocs-kroki-plugin",
    )
    val extraData = PythonArtifact(
        id = "mkdocs-markdownextradata-plugin",
    )
    val material = PythonArtifact(
        id = "mkdocs-material",
    )
    val minify = PythonArtifact(
        id = "mkdocs-minify-plugin",
    )
    val redirects = PythonArtifact(
        id = "mkdocs-redirects",
    )
    val pygments = PythonArtifact(
        id = "pygments",
    )
    val pymdown = PythonArtifact(
        id = "pymdown-extensions",
    )
}
