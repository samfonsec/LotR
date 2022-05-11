package com.samfonsec.lotr.view.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.samfonsec.lotr.R
import com.samfonsec.lotr.databinding.ActMainBinding
import com.samfonsec.lotr.util.extensions.viewBinding


class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).run {
            setupWithNavController(binding.bottomNavigation, navController)
            binding.toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))

            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.bottomNavigation.isVisible = destination.hasBottomNavigation()
            }
        }
    }

    private fun NavDestination.hasBottomNavigation() = id == R.id.home || id == R.id.favorite || id == R.id.security
}