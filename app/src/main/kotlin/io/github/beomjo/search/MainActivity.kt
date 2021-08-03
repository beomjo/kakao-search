package io.github.beomjo.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.github.beomjo.search.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = getNavHostFragment()
        navController = navHostFragment.navController

        setAppBarConfiguration()

        setupActionBar()

        setupBottomNavMenu()

        setupNavigationMenu()
    }

    private fun getNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.nav_host_container_fragment) as NavHostFragment

    private fun setAppBarConfiguration() {
        appBarConfiguration = if (binding.drawerLayout != null) {
            AppBarConfiguration(
                setOf(R.id.home_dest, R.id.search_dest),
                binding.drawerLayout
            )
        } else {
            AppBarConfiguration(navController.graph)
        }
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