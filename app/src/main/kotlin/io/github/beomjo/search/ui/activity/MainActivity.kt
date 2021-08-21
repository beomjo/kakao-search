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

package io.github.beomjo.search.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.beomjo.search.R
import io.github.beomjo.search.databinding.ActivityMainBinding
import io.github.beomjo.search.navigator.BottomNavigator
import java.lang.IllegalStateException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
    }
    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container_fragment)
                ?: throw IllegalStateException()
        navHostFragment.findNavController()
    }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(R.id.bookmark_dest, R.id.search_dest),
            binding.drawerLayout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        navController.apply {
            navigatorProvider.addNavigator(
                BottomNavigator(
                    R.id.nav_host_container_fragment,
                    supportFragmentManager
                )
            )

            setGraph(R.navigation.nav_graph)
        }

        setupActionBar()

        setupBottomNavMenu()

        setupNavigationMenu()
    }

    private fun setupActionBar() {
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavMenu() {
        binding.bottomNavView?.setupWithNavController(navController)
    }

    private fun setupNavigationMenu() {
        binding.navView?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        binding.navView ?: return false
        return super.onCreateOptionsMenu(menu)
    }
}
