/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.component

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskDependency

abstract class AntibytesCustomComponentPluginExtension : CustomComponentContract.Extension {
    private object NoopTask : DefaultTask()

    init {
        classifier.convention(null)
        date.convention(null)
        buildDependencies.convention(TaskDependency { emptySet() })
    }
}
