/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.helper

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class AntibytesDependencyPluginExtension : DependencyContract.DependencyPluginExtension {
    abstract override val keywords: SetProperty<String>
    abstract override val versionRegex: Property<Regex>

    init {
        keywords.convention(setOf("RELEASE", "FINAL", "GA"))
        versionRegex.convention("^[0-9,.v-]+(-r)?$".toRegex())
    }
}
