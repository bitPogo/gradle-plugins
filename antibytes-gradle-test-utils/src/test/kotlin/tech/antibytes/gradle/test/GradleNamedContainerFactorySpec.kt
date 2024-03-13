/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.test

import com.appmattus.kotlinfixture.kotlinFixture
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradleNamedContainerFactory.createNamedContainer

private data class NamedValue<T : Any>(
    val xName: String,
    val value: T,
) : Named {
    override fun getName(): String = xName
}

class GradleNamedContainerFactorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given createNamedContainer is called with a Collection of values it returns an NamedObjectContainer`() {
        // Given
        val values: Map<String, Int> = mapOf(
            UUID.randomUUID().toString() to fixture(),
            UUID.randomUUID().toString() to fixture(),
            UUID.randomUUID().toString() to fixture(),
        )

        // When
        val container = createNamedContainer(
            values.map { (name, value) ->
                NamedValue(name, value)
            },
        )

        // Then
        assertTrue { container is NamedDomainObjectContainer<*> }
        assertEquals(values.size, container.size)
        values.forEach { (name, value) ->
            assertEquals(
                value,
                container.named(name).get().value,
            )
        }
    }
}
