/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.docs

import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask
import tech.antibytes.gradle.configuration.ConfigurationContract

internal object DokkaConfigurator : ConfigurationContract.Configurator<Any> {
    override fun configure(project: Project, configuration: Any) {
        project.tasks.withType(DokkaTask::class.java).configureEach {
            outputDirectory.set(project.buildDir.resolve("dokka"))

            offlineMode.set(true)
            suppressObviousFunctions.set(true)

            dokkaSourceSets.configureEach {
                reportUndocumented.set(true)
                skipEmptyPackages.set(true)
                jdkVersion.set(8)
                noStdlibLink.set(false)
                noJdkLink.set(false)
                noAndroidSdkLink.set(false)
            }
        }
    }
}
