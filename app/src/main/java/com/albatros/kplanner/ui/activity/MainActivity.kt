package com.albatros.kplanner.ui.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration
            .Builder(R.id.EnterFragment, R.id.RegisterFragment, R.id.WelcomeFragment, R.id.DrawerFragment).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.dark_cyan, theme))
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val res = fragment.childFragmentManager.fragments.lastOrNull()
        val secChild = (res?.childFragmentManager?.fragments?.lastOrNull() as? NavHostFragment)?.childFragmentManager?.fragments?.lastOrNull()

        Log.d("AAAAAAAAA", "onBackPressed: ${res?.javaClass?.name ?: "null"} ${secChild?.javaClass?.name ?: "null"}")

        if (res !is IOnBackPressed) {
            if (secChild !is IOnBackPressed) {
                super.onBackPressed()
                return
            }
            secChild.onBackPressed()
            return
        }
       res.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}