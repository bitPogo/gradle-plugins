/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Transformer
import org.gradle.api.provider.Provider
import java.util.function.BiFunction

class TestNamedProvider<T>(
    private val value: T?
) : NamedDomainObjectProvider<T> {
    override fun get(): T = value!!

    override fun getOrNull(): T? = value

    override fun getOrElse(defaultValue: T): T = value ?: defaultValue

    override fun <S : Any?> map(transformer: Transformer<out S, in T>): Provider<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Any?> flatMap(transformer: Transformer<out Provider<out S>, in T>): Provider<S> {
        TODO("Not yet implemented")
    }

    override fun isPresent(): Boolean {
        TODO("Not yet implemented")
    }

    override fun orElse(value: T): Provider<T> {
        TODO("Not yet implemented")
    }

    override fun orElse(p0: Provider<out T>): Provider<T> {
        TODO("Not yet implemented")
    }

    override fun forUseAtConfigurationTime(): Provider<T> {
        TODO("Not yet implemented")
    }

    override fun <U : Any?, R : Any?> zip(p0: Provider<U>, p1: BiFunction<in T, in U, out R>): Provider<R> {
        TODO("Not yet implemented")
    }

    override fun configure(action: Action<in T>) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }
}
