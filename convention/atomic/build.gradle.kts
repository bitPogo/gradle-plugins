/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(libs.versions.java.jvm.get()).toString()))
    }

    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.jvm.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.jvm.get())
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.kotlinPoet)
}


gradlePlugin {
    plugins {
        register("tech.antibytes.gradle.configuration.java.local") {
            id = "tech.antibytes.gradle.configuration.java.local"
            displayName = "Java Configuration Plugin for Antibytes projects."
            implementationClass = "tech.antibytes.gradle.configuration.AntibytesJavaConfiguration"
            description = "Java Configuration Plugin for Antibytes projects."
        }

        plugins.register("tech.antibytes.gradle.runtime.local") {
            id = "tech.antibytes.gradle.runtime.local"
            implementationClass = "tech.antibytes.gradle.runtime.local.RuntimePlugin"
        }
    }
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "src-plugin/main/kotlin",
            "build/generated/antibytes/main/kotlin",
        )
    }
}

val provideConfig: Task by tasks.creating {
    mustRunAfter("clean")

    doFirst {
        val packaqe = "tech.antibytes.gradle.configuration.jvm.config"
        val template = File(layout.projectDirectory.asFile, "template/MainConfig.tmpl")
        val configDir = File(
            layout.buildDirectory.asFile.get(),
            "generated/antibytes/main/kotlin/${packaqe.replace('.', '/')}"
        )

        if (!configDir.exists()) {
            if (!configDir.mkdirs()) {
                logger.error("The plugin was not able to create the config directory at ${configDir.absolutePath}")
            }
        }

        val config = template
            .readText()
            .replace("PACKAGE", packaqe)
            .replace("JAVA_VERSION", libs.versions.java.jvm.get())

        File(configDir, "MainConfig.kt").writeText(config)
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}
