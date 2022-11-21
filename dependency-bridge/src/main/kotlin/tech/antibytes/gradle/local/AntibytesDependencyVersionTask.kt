/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import org.gradle.api.DefaultTask
import tech.antibytes.gradle.local.DependencyVersionContract.DependencyVersionTask

abstract class AntibytesDependencyVersionTask : DefaultTask(), DependencyVersionTask {

}
