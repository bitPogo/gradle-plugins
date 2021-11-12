/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task

import org.junit.Test
import tech.antibytes.gradle.coverage.CoverageContract
import kotlin.test.assertTrue

class TaskControllerSpec {
    @Test
    fun `It fulfils TaskController`() {
        val configurator: Any = TaskController

        assertTrue(configurator is CoverageContract.TaskController)
    }
}
