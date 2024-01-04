/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.config

object LibraryConfig {
    val publishing = PublishConfig

    val username = System.getenv("PACKAGE_REGISTRY_UPLOAD_USERNAME")?.toString() ?: ""
    val password = System.getenv("PACKAGE_REGISTRY_UPLOAD_TOKEN")?.toString() ?: ""

    const val group = "tech.antibytes.gradle"
    const val name = "gradle-plugins"

    const val githubOwner = "bitPogo"
    const val githubRepository = "gradle-plugins"

    object PublishConfig {
        const val description = "Plugins for gradle to ease project configurations."

        const val year = "2022"

        // URL
        private const val host = "github.com"
        private const val path = "$githubOwner/$githubRepository"

        const val url = "https://$host/$path"

        // DEVELOPER
        const val developerId = "bitPogo"
        const val developerName = "bitPogo"
        const val developerUrl = "https://$host/$githubOwner"
        const val developerEmail = "bitpogo@antibytes.tech"

        // LICENSE
        const val licenseName = "The Apache License, Version 2.0"
        const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
        const val licenseDistribution = "repo"

        // SCM
        private const val scmUrl = "git://$host/$path.git"
        private const val scmConnection = "scm:$scmUrl"
        const val scmDeveloperConnection = scmConnection
    }
}
