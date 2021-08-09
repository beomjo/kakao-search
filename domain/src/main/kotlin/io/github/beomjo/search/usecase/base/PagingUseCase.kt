package io.github.beomjo.search.usecase.base

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


abstract class PagingUseCase<in P, R: Any> {

    operator fun invoke(parameters: P): Flow<PagingData<R>> {
        return execute(parameters)
    }

    protected abstract fun execute(parameters: P): Flow<PagingData<R>>
}