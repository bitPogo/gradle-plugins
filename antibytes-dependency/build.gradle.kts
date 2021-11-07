import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
    id("tech.antibytes.gradle.plugin.script.publishing")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

object Version {
    const val kotlin = "1.5.31"
    const val junit = "5.8.1"
    const val mockk = "1.12.0"
    const val fixture = "1.2.0"
    const val dependencyUpdate = "0.39.0"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${Version.dependencyUpdate}")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}")
    testImplementation(platform("org.junit:junit-bom:${Version.junit}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:${Version.mockk}")
    testImplementation("com.appmattus.fixture:fixture:${Version.fixture}")
    testImplementation(project(":antibytes-plugin-test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.antibytes-dependency") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.antibytes-dependency"
        displayName = "${id}.gradle.plugin"
        implementationClass = "tech.antibytes.gradle.dependency.AntiBytesDependency"
        description = "General dependencies for Antibytes projects"
        version = "0.1.0"
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.named("test"))

    reports {
        html.isEnabled = true
        xml.isEnabled = true
        csv.isEnabled = true

        html.destination =
            layout.buildDirectory.dir("reports/jacoco/test/${project.name}").get().asFile
        csv.destination =
            layout.buildDirectory.file("reports/jacoco/test/${project.name}.csv").get().asFile
        xml.destination =
            layout.buildDirectory.file("reports/jacoco/test/${project.name}.xml").get().asFile
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.named("jacocoTestReport"))
    violationRules {
        rule {
            enabled = true
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = BigDecimal(0.70)
            }
        }
        rule {
            enabled = true
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = BigDecimal( 0.47)
            }
        }
    }
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}
