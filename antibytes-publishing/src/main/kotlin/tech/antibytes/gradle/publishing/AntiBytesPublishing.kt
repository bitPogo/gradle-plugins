package tech.antibytes.gradle.publishing

import org.gradle.api.Plugin
import org.gradle.api.Project

class AntiBytesPublishing : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create(
            "antiBytesPublishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        PublisherController.configure(target, extension)
    }
}
