/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality

internal abstract class Configurator : QualityContract.Configurator {
    protected fun <T : Any> T?.applyIfNotNull(action: (T) -> Unit) {
        this?.run {
            action(this)
        }
    }
}
