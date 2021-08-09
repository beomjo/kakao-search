package io.github.beomjo.search.usecase.base

abstract class UseCase<in P, R : Any> {

    operator fun invoke(parameters: P): R {
        return execute(parameters)
    }

    protected abstract fun execute(parameters: P): R
}