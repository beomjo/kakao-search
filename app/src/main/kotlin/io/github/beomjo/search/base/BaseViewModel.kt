/*
 * Designed and developed by 2021 beomjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.beomjo.search.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.beomjo.compilation.util.Event
import com.bumptech.glide.load.HttpException
import com.skydoves.bindables.BindingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : BindingViewModel() {
    val toast = MutableLiveData<Event<String>>()

    val event = MutableLiveData<Event<Action>>()

    fun launch(
        block: suspend () -> Unit,
    ): Job {
        return launchBlock(block, null)
    }

    fun launchWithErrorHandle(
        block: suspend () -> Unit,
        handleException: (() -> Unit?)? = null,
    ): Job {
        return launchBlock(block, handleException)
    }

    private fun launchBlock(
        block: suspend () -> Unit,
        handleException: (() -> Unit?)? = null,
    ): Job {
        return viewModelScope.launch {
            try {
                block()
            } catch (e: HttpException) {
                handleException?.let { handleException() }
                toast.value = Event(e.message ?: "Error")
            }
        }
    }

    fun finish() {
        event.value = Event(Action.Finish)
    }

    sealed class Action {
        object Finish : Action()
    }
}
