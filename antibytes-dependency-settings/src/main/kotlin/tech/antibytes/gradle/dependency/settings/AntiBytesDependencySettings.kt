/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import tech.antibytes.gradle.dependency.settings.config.MainConfig

@Suppress("UnstableApiUsage")
class AntiBytesDependencySettings : Plugin<Settings> {
    override fun apply(target: Settings) {
        target.dependencyResolutionManagement.apply {
            repositories.apply {
                maven {
                    setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
                    content {
                        includeGroup("tech.antibytes.gradle-plugins")
                    }
                }
                maven {
                    setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
                    content {
                        includeGroup("tech.antibytes.gradle-plugins")
                    }
                }
            }

            versionCatalogs.apply {
                create("antibytesCatalog").apply {
                    from(
                        "tech.antibytes.gradle-plugins:antibytes-dependency-catalog:${MainConfig.antibytesVersion}",
                    )
                }
            }
        }
    }
}
