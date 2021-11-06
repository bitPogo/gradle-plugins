pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("com.gradle.enterprise") version("3.7")
}

include(
    ":antibytes-plugin-test",
    ":antibytes-dependency",
    ":antibytes-publishing"
)

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "gradle-plugins"
