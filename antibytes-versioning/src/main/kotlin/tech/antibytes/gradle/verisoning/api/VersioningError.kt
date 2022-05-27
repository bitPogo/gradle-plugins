/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.verisoning.api

import org.gradle.api.GradleException

class VersioningError(
    override val message: String
) : GradleException(message)
