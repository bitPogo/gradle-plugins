import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
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
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(libs.versions.java.get()).toString()))
    }

    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

dependencies {
    implementation(libs.kotlin)
    implementation(projects.utilsJavaConfiguration)
}


gradlePlugin {
    plugins.create("tech.antibytes.gradle.configuration.java.local") {
        id = "tech.antibytes.gradle.configuration.java.local"
        displayName = "Java Configuration Plugin for Antibytes projects."
        implementationClass = "tech.antibytes.gradle.configuration.AntibytesJavaConfiguration"
        description = "Java Configuration Plugin for Antibytes projects."
    }
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin"
        )
    }
}

val provideConfig: Task by tasks.creating {
    mustRunAfter("clean")

    doFirst {
        val packaqe = "tech.antibytes.gradle.configuration.config"
        val template = File(layout.projectDirectory.asFile, "template/MainConfig.tmpl")
        val configDir = File(
            layout.buildDirectory.asFile.get(),
            "generated/antibytes/main/${packaqe.replace('.', '/')}"
        )

        if (!configDir.exists()) {
            if (!configDir.mkdirs()) {
                logger.error("The plugin was not able to create the config directory at ${configDir.absolutePath}")
            }
        }

        val config = template
            .readText()
            .replace("PACKAGE", packaqe)
            .replace("JAVA_VERSION", libs.versions.java.get())

        File(configDir, "MainConfig.kt").writeText(config)
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}
