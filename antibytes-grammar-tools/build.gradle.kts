/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent

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
    implementation(libs.jflex)
    implementation(libs.turtle)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.fixture)
    testImplementation(project(":antibytes-gradle-test-utils"))
    // Note: this is not necessary for tests itself, only for ide support of integrationTests
    testImplementation(gradleTestKit())

    integrationTestImplementation(libs.kotlinTest)
    integrationTestImplementation(platform(libs.junit))
    integrationTestImplementation(libs.jupiter)
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

val provideBisonExec by tasks.creating(Task::class.java) {
    doFirst {
        val exec = project.findProperty("bison.exec")
        val bisonExec = File("${projectDir.absolutePath.trimEnd('/')}/src/integration/resources/bisonExec")
        if (!bisonExec.exists()) {
            bisonExec.createNewFile()
        }

        if (exec is String) {
            bisonExec.writeText(exec)
        } else {
            bisonExec.writeText("/usr/bin/bison")
        }
    }
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn(provideBisonExec)
}

val integrationTests by tasks.creating(Test::class.java) {
    description = "Run integration tests"
    group = "Verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.check {
    dependsOn(integrationTests, "jacocoTestCoverageVerification")
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()

    testLogging {
        events(TestLogEvent.FAILED)
    }
}
