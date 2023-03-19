/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

internal interface AppleConfigurationApiContract {
    companion object {
        // iOS
        const val IOS_DEFAULT_DEVICE = "iPhone 15"

        // watchOS
        const val WATCH_DEFAULT_DEVICE = "Apple Watch Series 7 (45mm)"
    }
}
