/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.script

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure

/**
 * Versioning task to calculate the version based on git tags and branch names
 *
 * Install:
 *
 * You need to add following dependencies to the buildSrc/build.gradle.kts
 *
 * dependencies {
 *     implementation("com.palantir.gradle.gitversion:gradle-git-version:0.12.3")
 * }
 *
 * and ensure that the gradlePluginPortal is available
 *
 * repositories {
 *     gradlePluginPortal()
 * }
 *
 * Just add id("tech.antibytes.gradle.plugin.script.versioning") to your rootProject build.gradle.kts plugins
 *
 * plugins {
 *     id("tech.antibytes.gradle.plugin.script.versioning")
 * }
 *
 * Review the generated version:
 * - ./gradlew versionInfo
 */
plugins {
    id("com.palantir.git-version")
}

val versionDetails: Closure<VersionDetails> by extra
val patternNoQualifierBranch = "main|release/.*".toRegex()
val patternFeatureBranch = "feature/(.*)".toRegex()
val patternDependencyBotBranch = "dependabot/(.*)".toRegex()
val patternTicketNumber = "[A-Z]{2,8}-.*/(.*)".toRegex()

fun versionName(): String {
    val details = versionDetails()

    return when {
        details.branchName == null -> versionNameWithQualifier(details)
        patternNoQualifierBranch.matches(details.branchName) -> versionNameWithQualifier(details)
        patternFeatureBranch.matches(details.branchName) -> featureVersionName(details)
        patternDependencyBotBranch.matches(details.branchName) -> dependencyBotVersionName(details)
        else -> throw UnsupportedOperationException("branch name not supported: ${details.branchName}")
    }
}

fun featureVersionName(details: VersionDetails): String {
    val featureName = patternFeatureBranch
        .matchEntire(details.branchName)!!
        .groups[1]!!
        .value
        .let {
            if (patternTicketNumber.matches(it)) {
                patternTicketNumber.matchEntire(it)!!.groups[1]!!.value
            } else {
                it
            }
        }

    return versionNameWithQualifier(details, featureName)
}

fun dependencyBotVersionName(details: VersionDetails): String {
    val dependencyBotVersionName = patternDependencyBotBranch
        .matchEntire(details.branchName)!!
        .groups[1]!!
        .value
        .replace("_", "-")
        .replace("/", "-")

    return versionNameWithQualifier(details, "bump-$dependencyBotVersionName")
}

fun cleanVersionName(
    details: VersionDetails,
    qualifierName: String?
): String {
    val versionCleaned = details.version
        .substringBefore(".dirty")
        .let {
            if (details.commitDistance > 0) {
                it.substringBefore("-")
            } else {
                it
            }
        }

    return if (qualifierName.isNullOrBlank()) {
        "${versionCleaned}-SNAPSHOT"
    } else {
        "${versionCleaned}-${qualifierName}-SNAPSHOT"
    }
}

fun versionNameWithQualifier(
    details: VersionDetails,
    qualifierName: String? = null
): String {
    val version = if (!details.isCleanTag || details.commitDistance > 0) {
        cleanVersionName(details, qualifierName)
    } else {
        details.version
    }

    return version.substringAfter("v")
}

val versionInfo: Task by tasks.creating {
    group = "versioning"

    doLast {
        println("VersionName: ${versionName()}")
        println("VersionDetails: ${versionDetails()}")
    }
}

val setProjectVersion: Task by tasks.creating {
    doFirst {
        version = versionName()
    }
}

allprojects {
    afterEvaluate {
        version = versionName()
    }
}
