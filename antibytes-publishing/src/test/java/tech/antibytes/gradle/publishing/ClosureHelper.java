/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing;

import com.palantir.gradle.gitversion.VersionDetails;

import groovy.lang.Closure;

class ClosureHelper {
    static public Closure<VersionDetails> createClosure(VersionDetails value) {
        return new Closure<VersionDetails>(value) {
            public VersionDetails doCall(Object args) {
                return value;
            }
        };
    }
}
