/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.provider.Property

abstract class AntiBytesPublishingExtension : PublishingContract.PublishingConfiguration {
    abstract override val releasePattern: Property<Regex>
    abstract override val featurePattern: Property<Regex>
    abstract override val issuePattern: Property<Regex?>

    init {
        releasePattern.convention("main|release/.*".toRegex())
        featurePattern.convention("feature/(.*)".toRegex())
        issuePattern.convention(null)
    }
}
