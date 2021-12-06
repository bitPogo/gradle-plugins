/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.grammar.bison.BisonTask
import tech.antibytes.gradle.grammar.jflex.JFlexTask

class GrammarToolsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.create("jflex", JFlexTask::class.java) {
            group = "Code Generation"
            description = "Generates a scanner from an (Java)FlexFile"
        }

        target.tasks.create("bison", BisonTask::class.java) {
            group = "Code Generation"
            description = "Generates a parser from an Grammar File"
        }
    }
}
