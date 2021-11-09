package tech.antibytes.gradle.publishing

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.publishing.publisher.PublisherController

class AntiBytesPublishing : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            "antiBytesPublishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        if (!target.plugins.hasPlugin("com.palantir.git-version")) {
            target.plugins.apply("com.palantir.git-version")
        }
        if (!target.plugins.hasPlugin("maven-publish")) {
            target.plugins.apply("maven-publish")
        }

        PublisherController.configure(target, extension)
    }
}
