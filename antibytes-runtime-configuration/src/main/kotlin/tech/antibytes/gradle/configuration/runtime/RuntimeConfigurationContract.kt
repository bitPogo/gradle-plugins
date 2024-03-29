/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.runtime

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

internal interface RuntimeConfigurationContract {
    interface RuntimeConfigurationTask {
        /**
         *
         */
        @get:Input
        @get:Optional
        val configurationFilePrefix: Property<String>

        /**
         * Namespace where the generated File lives under
         * This property is required
         */
        @get:Input
        val packageName: Property<String>

        /**
         * other than common sourceSet prefix for KMP
         * Optional
         */
        @get:Input
        @get:Optional
        val sourceSetPrefix: Property<String>

        /**
         * Fields and their string values which the output Configuration must contain
         * Optional
         */
        @get:Input
        @get:Optional
        val stringFields: MapProperty<String, String>

        /**
         * Fields and their integer values which the output Configuration must contain
         * Optional
         */
        @get:Input
        @get:Optional
        val integerFields: MapProperty<String, Int>

        /**
         * Fields and their long values which the output Configuration must contain
         * Optional
         */
        @get:Input
        @get:Optional
        val longFields: MapProperty<String, Long>

        /**
         * Fields and their integer values which the output Configuration must contain
         * Optional
         */
        @get:Input
        @get:Optional
        val booleanFields: MapProperty<String, Boolean>

        /**
         * Generates a RuntimeConfig file with the given parameter
         * @throws StopExecutionException if no packageName was set
         */
        @TaskAction
        fun generate()
    }
}
