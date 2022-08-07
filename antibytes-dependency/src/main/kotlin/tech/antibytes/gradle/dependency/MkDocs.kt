/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

object MkDocs {
    const val includeMarkdown = "mkdocs-include-markdown-plugin:${MkDocsVersion.includeMarkdown}"
    const val kroki = "mkdocs-kroki-plugin:${MkDocsVersion.kroki}"
    const val extraData = "mkdocs-markdownextradata-plugin:${MkDocsVersion.extraData}"
    const val material = "mkdocs-material:${MkDocsVersion.material}"
    const val minify = "mkdocs-minify-plugin:${MkDocsVersion.minify}"
    const val redirects = "mkdocs-redirects:${MkDocsVersion.redirects}"
    const val pygments = "pygments:${MkDocsVersion.pygments}"
    const val pymdown = "pymdown-extensions:${MkDocsVersion.pymdown}"
}
