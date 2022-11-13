/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.module.node.Development
import tech.antibytes.gradle.dependency.module.node.Optional
import tech.antibytes.gradle.dependency.module.node.Peer
import tech.antibytes.gradle.dependency.module.node.Production

internal object Node {
    val production = Production
    val development = Development
    val peer = Peer
    val optional = Optional
}
