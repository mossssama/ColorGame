package com.newOs.colorCraze.ui.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.newOs.colorCraze.R
import com.newOs.colorCraze.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.fragmentContainer)

        setupNavigationDrawer(navController,binding)
    }

    override fun onSupportNavigateUp(): Boolean = isBackBtnActivated()


    /* Makes clicking on Menu button return as back */
    private fun isBackBtnActivated():Boolean = NavigationUI.navigateUp(this.findNavController(R.id.fragmentContainer),appBarConfiguration)

    private fun setupNavigationDrawer(navController: NavController,binding: ActivityMainBinding){
        NavigationUI.setupActionBarWithNavController(this,navController,binding.drawer)
        appBarConfiguration = AppBarConfiguration(navController.graph,binding.drawer)

        preventDrawerOpeningWhilePlaying(navController,binding)

        NavigationUI.setupWithNavController(binding.navigationDrawer,navController)
        binding.navigationDrawer.setupWithNavController(navController)
    }

    private fun preventDrawerOpeningWhilePlaying(navController: NavController,binding: ActivityMainBinding){
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            if (nd.id == nc.graph.startDestination) binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            else                                    binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

}

