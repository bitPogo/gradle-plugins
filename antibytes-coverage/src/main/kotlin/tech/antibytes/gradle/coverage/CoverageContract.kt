/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

internal interface CoverageContract {
    interface Extension {
        var jacocoVersion: String
        val platforms: MutableMap<String, CoverageApiContract.CoverageConfiguration>
    }

    interface DefaultConfigurationProvider {
        fun createDefaultCoverageConfiguration(): CoverageApiContract.CoverageConfiguration
    }
}
