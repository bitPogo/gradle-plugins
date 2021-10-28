package tech.antibytes.gradle.dependency

object GradlePlugin {
    const val android = "com.android.tools.build:gradle:${Version.gradle.android}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.gradle.kotlin}"
    const val owasp = "org.owasp:dependency-check-gradle:${Version.gradle.owasp}"
}
