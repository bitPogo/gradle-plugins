/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import tech.antibytes.gradle.quality.QualityApiContract.LinterConfiguration
import tech.antibytes.gradle.quality.QualityApiContract.CodeAnalysisConfiguration
import tech.antibytes.gradle.quality.QualityApiContract.StableApiConfiguration
import tech.antibytes.gradle.quality.QualityApiContract.SonarqubeConfiguration

internal interface QualityContract {
    interface Extension {
        val explicitApiFor: SetProperty<String>
        val linter: Property<LinterConfiguration>
        val codeAnalysis: Property<CodeAnalysisConfiguration>
        val stableApi: Property<StableApiConfiguration>
        val qualityGate: Property<SonarqubeConfiguration>
    }
}
