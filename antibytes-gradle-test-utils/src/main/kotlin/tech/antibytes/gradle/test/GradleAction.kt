/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.gradle.test

import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.slot
import org.gradle.api.Action

/**
 * Captures a given Gradle Action Call
 * @param (Action<T>) -> T: Closure, which contains the Gradle Action Call which is meant to be execute
 * @param T: Object which is wrapped by the Action
 * @param T: return value of the Gradle Action
 */
inline fun <T : Any, reified R> invokeGradleAction(
    probe: T,
    returnValue: R,
    crossinline caller: MockKMatcherScope.(Action<T>) -> R,
) {
    val action = slot<Action<T>>()
    every {
        caller(capture(action))
    } answers {
        action.captured.execute(probe)
        returnValue
    }
}

/**
 * Captures a given Gradle Action Call
 * @param (Action<T>) -> T: Closure, which contains the Gradle Action Call which is meant to be execute
 * @param T: Object which is wrapped by the Action
 */
inline fun <T : Any> invokeGradleAction(
    probe: T,
    crossinline caller: MockKMatcherScope.(Action<T>) -> Unit,
) {
    val action = slot<Action<T>>()
    every {
        caller(capture(action))
    } answers {
        action.captured.execute(probe)
    }
}
