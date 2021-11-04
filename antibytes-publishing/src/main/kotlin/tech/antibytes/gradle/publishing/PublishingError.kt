/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.GradleException

sealed class PublishingError(
    override val message: String
) : GradleException(message) {
    class VersioningError(override val message: String) : PublishingError(message)
    class GitTooManyRepositoriesError(override val message: String) : PublishingError(message)
}
