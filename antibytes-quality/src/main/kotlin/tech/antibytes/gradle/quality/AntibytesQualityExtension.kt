/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality

import tech.antibytes.gradle.quality.api.LinterConfiguration

abstract class AntibytesQualityExtension : QualityContract.Extension {
    init {
        linter.convention(LinterConfiguration())
        codeAnalysis.convention(null)
        stableApi.convention(null)
        qualityGate.convention(null)
    }
}
