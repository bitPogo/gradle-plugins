package tech.antibytes.gradle.publishing

import org.gradle.api.Plugin
import org.junit.Test
import kotlin.test.assertTrue

class AntiBytesPublishingSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesPublishing()

        assertTrue(plugin is Plugin<*>)
    }
}
