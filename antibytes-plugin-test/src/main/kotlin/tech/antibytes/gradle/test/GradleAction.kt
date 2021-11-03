/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.test

import io.mockk.every
import io.mockk.slot
import org.gradle.api.Action

inline fun <T, reified R> invokeGradleAction(
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

inline fun <T> invokeGradleAction(
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
