/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.api

import org.gradle.api.Project
import tech.antibytes.gradle.quality.QualityApiContract

data class QualityGateConfiguration(
    override val projectKey: String,
    override val organization: String = "antibytes",
    override val host: String = "https://sonarcloud.io",
    override val encoding: String = "UTF-8",
    override val jacoco: String = "**/reports/jacoco/**/*.xml",
    override val junit: String = "**/test-results/jvmTest,**/test-results/testDebugUnitTest,",
    override val detekt: String,
    override val exclude: Set<String> = emptySet(),
) : QualityApiContract.QualityGateConfiguration {
    constructor(
        project: Project,
        projectKey: String,
        organization: String = "antibytes",
        host: String = "https://sonarcloud.io",
        encoding: String = "UTF-8",
        jacoco: String = "**/reports/jacoco/**/*.xml",
        junit: String = "**/test-results/jvmTest,**/test-results/testDebugUnitTest,",
        exclude: Set<String> = emptySet(),
    ) : this(
        projectKey = projectKey,
        organization = organization,
        host = host,
        encoding = encoding,
        jacoco = jacoco,
        junit = junit,
        exclude = exclude,
        detekt = "${project.layout.buildDirectory}/reports/detekt/detekt.xml",
    )
}
