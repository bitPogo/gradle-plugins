/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality

import org.gradle.api.Project
import org.gradle.api.provider.Property
import tech.antibytes.gradle.quality.QualityApiContract.CodeAnalysisConfiguration
import tech.antibytes.gradle.quality.QualityApiContract.LinterConfiguration
import tech.antibytes.gradle.quality.QualityApiContract.QualityGateConfiguration
import tech.antibytes.gradle.quality.QualityApiContract.StableApiConfiguration

internal interface QualityContract {
    interface Extension {
        val linter: Property<LinterConfiguration>
        val codeAnalysis: Property<CodeAnalysisConfiguration>
        val stableApi: Property<StableApiConfiguration>
        val qualityGate: Property<QualityGateConfiguration>
    }

    fun interface Configurator {
        fun configure(project: Project, configuration: Extension)
    }
}
