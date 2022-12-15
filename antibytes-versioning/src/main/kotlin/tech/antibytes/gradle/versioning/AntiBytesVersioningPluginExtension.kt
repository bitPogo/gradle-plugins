/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import org.gradle.api.Project
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

abstract class AntiBytesVersioningPluginExtension(
    private val project: Project,
) : VersioningInternalContract.Extension {
    private var _configuration: VersioningConfiguration? = null

    override var configuration: VersioningConfiguration?
        get() = _configuration
        set(value) {
            propagateVersion(value)
            _configuration = value
        }

    private fun propagateVersion(configuration: VersioningConfiguration?) {
        project.version = if (configuration != null) {
            Versioning.getInstance(project, configuration).versionName()
        } else {
            "unspecified"
        }
    }
}
