/*
*
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

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.beomjo.compilation.util.EventObserver
import com.skydoves.bindables.BindingActivity

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes contentLayoutId: Int,
) : BindingActivity<T>(contentLayoutId), LifecycleOwner {

    inline fun <reified T : BaseViewModel> getViewModel(): Lazy<T> {
        return lazy {
            ViewModelProvider(this)
                .get(T::class.java)
                .apply { observeViewModel(this) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLifeCycleOwner()
    }

    private fun bindingLifeCycleOwner() {
        binding {
            lifecycleOwner = this@BaseActivity
        }
    }

    fun observeViewModel(viewModel: BaseViewModel) {
        observeToast(viewModel)
    }

    private fun observeToast(vm: BaseViewModel) {
        vm.toast.observe(this, EventObserver { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })
        vm.event.observe(this, EventObserver {
            when (it) {
                is BaseViewModel.Action.Finish -> onBackPressed()
            }
        })
    }
}
