plugins {
    id("tech.antibytes.gradle.plugin.script.quality-spotless")
    id("tech.antibytes.gradle.plugin.script.versioning")
    id("org.owasp.dependencycheck")

    id("tech.antibytes.gradle.plugin.script.maven-package")
    id("tech.antibytes.gradle.plugin.script.publishing")
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.2"
    distributionType = Wrapper.DistributionType.ALL
}
