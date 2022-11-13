/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.node

import tech.antibytes.gradle.dependency.NodeArtifact

internal object Development {
    val copyWebpackPlugin = NodeArtifact(id = "copy-webpack-plugin")
}
