/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
import tech.antibytes.gradle.plugin.dependency.Dependency as AntibytesDependency
import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("idea")
    id("tech.antibytes.gradle.plugin.script.maven-package")
}

// To make it available as direct dependency
group = "tech.antibytes.gradle.grammar"

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output

        java.srcDir("src/integration/kotlin")
        resources.srcDir("src/integration/resources")
    }
}

idea {
    module {
        project.sourceSets["integrationTest"].java.srcDirs.forEach { src ->
            testSourceDirs.add(src)
        }
        project.sourceSets["integrationTest"].resources.srcDirs.forEach { src ->
            testResourceDirs.add(src)
        }
    }
}

fun DependencyHandler.integrationTestImplementation(dependencyNotation: Any): Dependency? =
    add("integrationTestImplementation", dependencyNotation)

dependencies {
    implementation(AntibytesDependency.gradle.kotlin)
    implementation(AntibytesDependency.library.jflex)
    implementation(AntibytesDependency.library.turtle)

    testImplementation(AntibytesDependency.test.kotlinTest)
    testImplementation(platform(AntibytesDependency.test.junit))
    testImplementation(AntibytesDependency.test.jupiter)
    testImplementation(AntibytesDependency.test.mockk)
    testImplementation(AntibytesDependency.test.fixture)
    testImplementation(project(":antibytes-test-utils"))
    // Note: this is not necessary for tests itself, only for ide support of integrationTests
    testImplementation(gradleTestKit())

    integrationTestImplementation(AntibytesDependency.test.kotlinTest)
    integrationTestImplementation(platform(AntibytesDependency.test.junit))
    integrationTestImplementation(AntibytesDependency.test.jupiter)
    integrationTestImplementation(gradleTestKit())
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.gradle.grammar") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.gradle.grammar"
        implementationClass = "tech.antibytes.gradle.grammar.GrammarToolsPlugin"
        description = "A Bison and JFlex plugin for Gradle"
        version = "0.1.0"
    }
    testSourceSets(sourceSets.getByName("integrationTest"))
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.named("jacocoTestReport"))
    violationRules {
        rule {
            enabled = true
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = BigDecimal(0.99)
            }
        }
        rule {
            enabled = true
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = BigDecimal( 0.99)
            }
        }
    }
}


val integrationTests by tasks.creating(Test::class.java) {
    description = "Run integration tests"
    group = "Verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}
