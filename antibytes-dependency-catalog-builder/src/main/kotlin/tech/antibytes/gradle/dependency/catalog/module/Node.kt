/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.module.node.Development
import tech.antibytes.gradle.dependency.catalog.module.node.Optional
import tech.antibytes.gradle.dependency.catalog.module.node.Peer
import tech.antibytes.gradle.dependency.catalog.module.node.Production

internal object Node {
    val production = Production
    val development = Development
    val peer = Peer
    val optional = Optional
}
