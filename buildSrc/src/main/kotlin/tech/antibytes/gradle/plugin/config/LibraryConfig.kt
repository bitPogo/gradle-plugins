/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.config

object LibraryConfig {
    val publishing = PublishConfig

    const val group = "tech.antibytes"
    const val name = "gradle-plugins"

    const val githubOwner = "bitPogo"
    const val githubRepository = "gradle-plugins"

    object PublishConfig {
        const val groupId = "tech.antibytes.gradle-plugins"
        const val description = "Plugins for gradle to ease project configurations."

        const val year = "2021"

        // URL
        const val host = "github.com"
        const val path = "$githubOwner/$githubRepository"

        const val url = "https://$host/$path"

        // DEVELOPER
        const val developerId = "bitPogo"
        const val developerName = "bitPogo"
        const val developerEmail = "solascriptura001+antibytes@gmail.com"

        // LICENSE
        const val licenseName = "The Apache License, Version 2.0"
        const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
        const val licenseDistribution = "repo"

        // SCM
        const val scmUrl = "git://$host/$path.git"
        const val scmConnection = "scm:$scmUrl"
        const val scmDeveloperConnection = scmConnection
    }
}
