/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.util.isAndroidLibrary

class AntiBytesConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.isAndroidLibrary()) {
            AndroidLibraryConfigurator.configure(
                target,
                DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(target)
            )
        }
    }
}
