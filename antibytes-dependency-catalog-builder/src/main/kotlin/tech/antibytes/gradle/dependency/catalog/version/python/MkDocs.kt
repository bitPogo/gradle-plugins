/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.version.python

import tech.antibytes.gradle.dependency.config.PythonVersions

internal object MkDocs {
    /**
     * [MkDocs Include Plugin](https://github.com/mondeja/mkdocs-include-markdown-plugin/releases)
     */
    const val includeMarkdown = PythonVersions.mkdocsIncludeMarkdownPlugin

    /**
     * [MkDocs Kroki](https://pypi.org/project/mkdocs-kroki-plugin/)
     */
    const val kroki = PythonVersions.mkdocsKrokiPlugin

    /**
     * [MkDocs Extra Injected Data](https://github.com/rosscdh/mkdocs-markdownextradata-plugin/releases)
     */
    const val extraData = PythonVersions.mkdocsMarkdownextradataPlugin

    /**
     * [MkDocs Minfier](https://pypi.org/project/mkdocs-minify-plugin/)
     */
    const val minify = PythonVersions.mkdocsMinifyPlugin

    /**
     * [MkDocs Redirects](https://github.com/mkdocs/mkdocs-redirects/releases)
     */
    const val redirects = PythonVersions.mkdocsRedirects

    /**
     * [Pygments](https://pypi.org/project/Pygments/)
     */
    const val pygments = PythonVersions.pygments

    /**
     * (PyMdown)(https://github.com/facelessuser/pymdown-extensions/releases)
     */
    const val pymdown = PythonVersions.pymdownExtensions
}
