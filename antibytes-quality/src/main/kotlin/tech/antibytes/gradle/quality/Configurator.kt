/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality

abstract class Configurator : QualityContract.Configurator {
    protected fun <T : Any> T?.applyIfNotNull(action: (T) -> Unit) {
        if (this != null) {
            action(this)
        }
    }
}
