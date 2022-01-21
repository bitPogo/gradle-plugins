/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.api

import org.gradle.api.file.ConfigurableFileTree
import tech.antibytes.gradle.coverage.CoverageApiContract
import java.io.File

data class AndroidJacocoConfiguration(
    override val reportSettings: CoverageApiContract.JacocoReporterSettings,
    override var testDependencies: Set<String>,
    override var classPattern: Set<String>,
    override var classFilter: Set<String>,
    override var sources: Set<File>,
    override var additionalSources: Set<File>,
    override var additionalClasses: ConfigurableFileTree? = null,
    override var verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
    override var instrumentedTestDependencies: Set<String>,
    override var variant: String,
    override var flavour: String,
) : CoverageApiContract.AndroidJacocoCoverageConfiguration
