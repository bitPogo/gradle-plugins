/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.component

import java.io.File
import java.util.Date
import org.gradle.api.tasks.TaskDependency

interface CustomComponentApiContract {
    data class Artifact(
        val buildDependencies: TaskDependency = TaskDependency { emptySet() },
        val name: String,
        val typeExtension: String,
        val type: String,
        val classifier: String? = null,
        val componentHandle: File,
        val date: Date? = null,
    )
}
