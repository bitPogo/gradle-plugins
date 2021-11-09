/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

abstract class AntiBytesCoverageExtension : CoverageContract.Extension {
    override val platforms: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf()
    override var jacocoVersion: String = "0.8.7"
}
