/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.settings

fun isGitHub(): Boolean = System.getenv("GITHUB")?.let { true } ?: false

fun isAzureDevops(): Boolean = System.getenv("AZURE_HTTP_USER_AGENT")?.let { true } ?: false
