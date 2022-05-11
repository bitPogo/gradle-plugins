/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

interface SigningApiContract {
    interface MemorySigning {
        val keyPath: String?
        val password: String?
    }

    interface CompleteMemorySigning : MemorySigning {
        val keyId: String?
    }
}
