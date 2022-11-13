/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

@file:Suppress("UnstableApiUsage")

package tech.antibytes.gradle.dependency

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.DependencyResolutionManagement

class AntibytesDependency : Plugin<Settings> {
    private fun DependencyResolutionManagement.addSharedAntibytesConfiguration() {
        versionCatalogs {
            create("antibytes") {
                addVersions()
                addDependencies()
            }
        }
    }

    override fun apply(target: Settings) = target.dependencyResolutionManagement.addSharedAntibytesConfiguration()
}
