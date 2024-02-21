/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.android.AndroidLibraryConfigurator
import tech.antibytes.gradle.configuration.android.CompileTaskConfigurator
import tech.antibytes.gradle.configuration.android.DefaultAndroidLibraryConfigurationProvider
import tech.antibytes.gradle.configuration.android.ToolChainConfigurator
import tech.antibytes.gradle.util.applyIfNotExists

class AntibytesLibraryConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyIfNotExists("com.android.library")

        AndroidLibraryConfigurator.configure(
            target,
            DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(target),
        )

        CompileTaskConfigurator.configure(target)
        ToolChainConfigurator.configure(target)
    }
}
