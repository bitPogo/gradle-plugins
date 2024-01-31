/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import tech.antibytes.gradle.dependency.settings.config.MainConfig

@Suppress("UnstableApiUsage")
class AntibytesDependencySettings : Plugin<Settings> {
    override fun apply(target: Settings) {
        target.dependencyResolutionManagement.apply {
            repositories.apply {
                maven {
                    setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
                    content {
                        includeGroupByRegex(MainConfig.pluginGroup)
                    }
                }
                maven {
                    setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
                    content {
                        includeGroupByRegex(MainConfig.pluginGroup)
                    }
                }
                maven {
                    setUrl(MainConfig.gradlePluginsDir)
                    content {
                        includeGroupByRegex(MainConfig.pluginGroup)
                    }
                }
                gradlePluginPortal()
                mavenCentral()
                google()
            }

            versionCatalogs.apply {
                create("antibytesCatalog").apply {
                    from(
                        "tech.antibytes.gradle:antibytes-dependency-catalog:${MainConfig.antibytesVersion}",
                    )
                }
            }
        }

        target.plugins.apply(MainConfig.toolchainPluginId)
    }
}
