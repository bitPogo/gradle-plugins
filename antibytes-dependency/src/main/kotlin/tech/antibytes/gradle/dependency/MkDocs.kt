/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import tech.antibytes.gradle.dependency.version.MkDocs

object MkDocs {
    const val includeMarkdown = "mkdocs-include-markdown-plugin:${MkDocs.includeMarkdown}"
    const val kroki = "mkdocs-kroki-plugin:${MkDocs.kroki}"
    const val extraData = "mkdocs-markdownextradata-plugin:${MkDocs.extraData}"
    const val material = "mkdocs-material:${MkDocs.material}"
    const val minify = "mkdocs-minify-plugin:${MkDocs.minify}"
    const val redirects = "mkdocs-redirects:${MkDocs.redirects}"
    const val pygments = "pygments:${MkDocs.pygments}"
    const val pymdown = "pymdown-extensions:${MkDocs.pymdown}"
}
