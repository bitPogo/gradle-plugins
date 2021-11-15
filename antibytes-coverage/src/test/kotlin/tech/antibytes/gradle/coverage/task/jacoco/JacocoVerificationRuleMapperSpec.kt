/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.internal.jacoco.rules.JacocoViolationRuleImpl
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
import org.junit.Test
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JacocoVerificationRuleMapperSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils JacocoVerificationRuleMapper`() {
        val mapper: Any = JacocoVerificationRuleMapper

        assertTrue(mapper is JacocoContract.JacocoVerificationRuleMapper)
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and an empty Set of Rules, it returns a empty Set`() {
        // Given
        val generator: JacocoViolationRulesContainer = mockk()
        val rules = emptySet<CoverageApiContract.JacocoVerificationRule>()

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        verify(exactly = 0) { generator.rule(any()) }
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules and the rules contain no values, it returns a Set of default JacocoViolationRules`() {
        // Given
        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(),
            JacocoVerificationRule()
        )

        val actual1 = JacocoViolationRuleImpl()
        val actual2 = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual1

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual1,
            expected = JacocoViolationRuleImpl()
        )

        assertEquals(
            actual = actual2,
            expected = JacocoViolationRuleImpl()
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contains a Scope, it returns a set of JacocoViolationRules, with a customized Scope`() {
        // Given
        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                scope = CoverageApiContract.JacocoScope.METHOD
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.element,
            expected = "METHOD"
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contains a Flag for enable the rule, it returns a set of JacocoViolationRules, with a customized Flag`() {
        // Given
        val enabled: Boolean = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                enable = enabled
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.isEnabled,
            expected = enabled
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contains Path of included Files, it returns a set of JacocoViolationRules, with the given Includes`() {
        // Given
        val included: Set<String> = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                includes = included
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.includes.toSet(),
            expected = included
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contains Path of Excludes Files, it returns a set of JacocoViolationRules, with the given Excludes`() {
        // Given
        val excludes: Set<String> = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                excludes = excludes
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.excludes.toSet(),
            expected = excludes
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, it will not set a limit if no minimum or maximum are are present`() {
        // Given
        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                counter = CoverageApiContract.JacocoCounter.BRANCH
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.limits.size,
            expected = 0
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contain a Minimum, it sets the Minimum of the Limit`() {
        // Given
        val minimum: BigDecimal = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                minimum = minimum,
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.limits[0].minimum,
            expected = minimum
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contain a Maximum, it sets the Maximum of the Limit`() {
        // Given
        val maximum: BigDecimal = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                maximum = maximum,
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.limits[0].maximum,
            expected = maximum
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contain a Maximum and a Counter, it sets the Counter of the Limit`() {
        // Given
        val maximum: BigDecimal = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                maximum = maximum,
                counter = CoverageApiContract.JacocoCounter.LINE
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.limits[0].counter,
            expected = "LINE"
        )
    }

    @Test
    fun `Given map is called with a JacocoRuleGenerator and a Set of Rules, which contain a Maximum and a Measurement, it sets the Measurement of the Limit`() {
        // Given
        val maximum: BigDecimal = fixture()

        val generator: JacocoViolationRulesContainer = mockk()
        val rules = setOf(
            JacocoVerificationRule(
                maximum = maximum,
                measurement = CoverageApiContract.JacocoMeasurement.MISSED_COUNT
            )
        )

        val actual = JacocoViolationRuleImpl()

        every { generator.rule(any()) } returns actual

        // When
        JacocoVerificationRuleMapper.map(generator, rules)

        // Then
        assertEquals(
            actual = actual.limits[0].value,
            expected = "MISSEDCOUNT"
        )
    }
}
