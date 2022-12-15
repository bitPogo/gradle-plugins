/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.versioning

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.versioning.VersioningInternalContract.Companion.EXTENSION_ID
import tech.antibytes.gradle.versioning.VersioningInternalContract.Extension

class AntibytesVersioning : Plugin<Project> {
    private fun Project.applyGitVersionPlugin() {
        if (!plugins.hasPlugin("com.palantir.git-version")) {
            plugins.apply("com.palantir.git-version")
        }
    }

    private fun Project.createExtension(): Extension {
        return extensions.create(EXTENSION_ID, AntiBytesVersioningPluginExtension::class.java)
    }

    private fun Project.addVersion(extension: Extension) {
        afterEvaluate {
            extension.configuration.orNull?.also { configuration ->
                version = Versioning.getInstance(this@addVersion, configuration).versionName()
            }
        }
    }

    override fun apply(target: Project) {
        target.applyGitVersionPlugin()
        val extension = target.createExtension()

        target.addVersion(extension)
    }
}
