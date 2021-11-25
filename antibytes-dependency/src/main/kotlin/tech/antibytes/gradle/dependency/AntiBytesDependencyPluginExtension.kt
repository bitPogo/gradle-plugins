/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class AntiBytesDependencyPluginExtension : DependencyContract.DependencyPluginExtension {
    abstract override val keywords: SetProperty<String>
    abstract override val versionRegex: Property<Regex>

    init {
        keywords.convention(setOf("RELEASE", "FINAL", "GA"))
        versionRegex.convention("^[0-9,.v-]+(-r)?$".toRegex())
    }
}
