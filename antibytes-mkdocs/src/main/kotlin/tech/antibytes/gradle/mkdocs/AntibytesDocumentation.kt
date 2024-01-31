/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.mkdocs

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.register
import ru.vyarus.gradle.plugin.mkdocs.MkdocsExtension
import ru.vyarus.gradle.plugin.python.PythonExtension
import tech.antibytes.gradle.mkdocs.MkdocsContract.Companion.EXTENSION_ID
import tech.antibytes.gradle.mkdocs.config.MainConfig
import tech.antibytes.gradle.util.applyIfNotExists
import tech.antibytes.gradle.versioning.Versioning

class AntibytesDocumentation : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyIfNotExists(
            "ru.vyarus.mkdocs",
            "tech.antibytes.gradle.versioning",
        )

        val extension = target.extensions.create(EXTENSION_ID, AntibytesDocumentationExtension::class.java)

        target.extensions.getByType(PythonExtension::class.java).apply {
            if (OperatingSystem.current().isMacOsX) {
                envPath = "${target.rootDir.absolutePath.trimEnd('/')}/.gradle/python/opt/homebrew/"
            }

            pip(
                MainConfig.includeMarkdown,
                MainConfig.kroki,
                MainConfig.extraData,
                MainConfig.minify,
                MainConfig.redirects,
                MainConfig.pygments,
                MainConfig.pymdown,
            )
        }

        target.tasks.register<Delete>("clean") {
            delete("build")
        }

        target.afterEvaluate {
            val version = Versioning.getInstance(
                project = target,
                configuration = extension.versioning.get(),
            ).versionName()

            target.extensions.getByType(MkdocsExtension::class.java).apply {
                sourcesDir = projectDir.absolutePath

                publish.docPath = version
                if (version.endsWith("SNAPSHOT")) {
                    publish.rootRedirect = false
                } else {
                    publish.rootRedirect = true
                    publish.rootRedirectTo = "latest"
                    publish.setVersionAliases("latest")
                }
                publish.generateVersionsFile = true

                strict = true

                extras = mapOf(
                    "version" to version,
                )
            }
        }
    }
}
