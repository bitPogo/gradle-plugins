/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.test

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
    crossinline caller: (Action<T>) -> R,
    probe: T,
    returnValue: R
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
    crossinline caller: (Action<T>) -> Unit,
    probe: T
) {
    val action = slot<Action<T>>()
    every {
        caller(capture(action))
    } answers {
        action.captured.execute(probe)
    }
}
