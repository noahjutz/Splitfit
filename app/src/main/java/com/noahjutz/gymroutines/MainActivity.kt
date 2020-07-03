package com.noahjutz.gymroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("unused")
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
        initAppBar()
    }

    private fun initAppBar() {
        setSupportActionBar(app_bar)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.routinesFragment,
            R.id.exercisesFragment,
            R.id.profileFragment
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initBottomNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(
            navController
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) ||
            super.onSupportNavigateUp()
    }
}
