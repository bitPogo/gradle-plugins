/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.source

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.coverage.configuration.makePath
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver
import java.io.File

internal object SourceHelper : ConfigurationContract.SourceHelper {
    private fun List<String>.toFile(): File {
        return File(makePath(this))
    }

    private fun mapSourceFiles(source: File): Set<File> {
        return source.walkBottomUp().toSet()
    }

    private fun resolveSourceFiles(sources: ConfigurationContract.SourceContainer): Set<File> {
        return mutableSetOf<File>()
            .apply {
                this.addAll(mapSourceFiles(sources.platform))
            }
            .apply {
                if (sources.common is File) {
                    this.addAll(mapSourceFiles(sources.common))
                }
            }
    }

    private fun mergeSource(
        platform: List<String>,
        toAdd: String
    ): File {
        val merged = platform.toMutableList().also { it.add(toAdd) }
        return merged.toFile()
    }

    private fun resolveCommon(platform: List<String>): File {
        return mergeSource(platform, "commonMain")
    }

    private fun resolvePlatformSource(platform: List<String>): File {
        return mergeSource(platform, "main")
    }

    private fun resolvePlatformSourceKmp(
        context: PlatformContext,
        platform: List<String>
    ): File {
        return mergeSource(platform, "${context.prefix}Main")
    }

    override fun resolveSources(
        project: Project,
        context: PlatformContext
    ): Set<File> {
        val platform = listOf(
            project.projectDir.absolutePath.trimEnd(File.separatorChar),
            "src"
        )

        val sourceSets = if (PlatformContextResolver.isKmp(context)) {
            ConfigurationContract.SourceContainer(
                platform = resolvePlatformSourceKmp(context, platform),
                common = resolveCommon(platform)
            )
        } else {
            ConfigurationContract.SourceContainer(
                platform = resolvePlatformSource(platform)
            )
        }

        return resolveSourceFiles(sourceSets)
    }
}
