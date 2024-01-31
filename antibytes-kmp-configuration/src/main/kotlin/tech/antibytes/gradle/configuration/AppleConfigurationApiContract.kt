/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

internal interface AppleConfigurationApiContract {
    companion object {
        // iOS
        const val IOS_14 = "iPhone 14"
        const val IOS_15 = "iPhone 15"

        // watchOS
        const val WATCH_7 = "Apple Watch Series 7 (45mm)"
    }
}
