package com.noahjutz.gymroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.noahjutz.gymroutines.ui.exercises.ExercisesFragmentDirections
import com.noahjutz.gymroutines.ui.routines.RoutinesFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*

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
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.routinesFragment,
            R.id.exercisesFragment
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
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}