/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.config.NodeDevelopmentVersions
import tech.antibytes.gradle.dependency.config.NodeProductionVersions

internal object Node {
    /**
     * [Axios Client](https://github.com/axios/axios)
     */
    const val axios = NodeProductionVersions.axios

    /**
     * [Copy Webpack Plugin](https://www.npmjs.com/package/copy-webpack-plugin)
     */
    const val copyWebpackPlugin = NodeDevelopmentVersions.copyWebpackPlugin

    /**
     * [SQL Js](https://github.com/sql-js/sql.js/)
     */
    const val sqlJs = NodeProductionVersions.sqlJs
}
