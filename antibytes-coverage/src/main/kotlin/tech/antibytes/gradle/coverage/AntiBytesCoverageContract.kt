/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

internal interface AntiBytesCoverageContract {
    interface Extension {
        val jacocoVersion: String
        val coverageBranches: Map<String, AntiBytesCoverageApiContract.CoverageConfiguration>
    }
}
