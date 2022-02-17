/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.runtime

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

internal interface RuntimeConfigurationContract {
    interface RuntimeConfigurationTask {
        /**
         * Namespace where the generated File lives under
         * This property is required
         */
        @get:Input
        val packageName: Property<String>

        /**
         * Fields and their string values which the output Configuration must contain
         */
        @get:Input
        val stringFields: MapProperty<String, String>

        /**
         * Fields and their integer values which the output Configuration must contain
         */
        @get:Input
        val integerFields: MapProperty<String, Int>
    }
}
