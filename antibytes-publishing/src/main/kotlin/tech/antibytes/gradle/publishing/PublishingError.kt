/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.GradleException

sealed class PublishingError(
    override val message: String,
) : GradleException(message) {
    class GitRejectedCommitError(override val message: String) : PublishingError(message)
}
