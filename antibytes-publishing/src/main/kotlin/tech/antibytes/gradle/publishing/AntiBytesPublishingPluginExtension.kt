/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

internal abstract class AntiBytesPublishingPluginExtension : PublishingContract.PublishingPluginConfiguration {
    // Versioning
    abstract override val releasePattern: Property<Regex>
    abstract override val featurePattern: Property<Regex>
    abstract override val dependencyBotPattern: Property<Regex>
    abstract override val issuePattern: Property<Regex?>
    abstract override val versionPrefix: Property<String>
    abstract override val normalization: SetProperty<String>

    abstract override val publishingConfigurations: Property<PublishingApiContract.PublishingConfiguration?>

    init {
        releasePattern.convention("main|release/.*".toRegex())
        featurePattern.convention("feature/(.*)".toRegex())
        dependencyBotPattern.convention("dependabot/(.*)".toRegex())
        issuePattern.convention(null)

        versionPrefix.convention("v")
        normalization.convention(emptySet())

        publishingConfigurations.convention(null)
    }
}
