/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract

internal object TaskController : CoverageContract.TaskController {
    override fun configure(
        project: Project,
        configurations: MutableMap<String, CoverageApiContract.CoverageConfiguration>
    ) {
        TODO("Not yet implemented")
    }
}
