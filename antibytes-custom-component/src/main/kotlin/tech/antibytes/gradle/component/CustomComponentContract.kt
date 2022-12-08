/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.component

import java.util.Date
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskDependency

internal interface CustomComponentContract {
    interface Extension {
        val buildDependencies: Property<TaskDependency>
        val name: Property<String>
        val typeExtension: Property<String>
        val type: Property<String>
        val classifier: Property<String?>
        val componentHandle: RegularFileProperty
        val date: Property<Date?>
        val attributes: MapProperty<Any, Any>
    }

    companion object {
        const val EXTENSION_ID = "antibytesCustomComponent"
    }
}
