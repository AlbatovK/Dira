package com.albatros.kplanner.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ActivityMainBinding
import com.albatros.kplanner.model.repo.PreferencesRepository
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (viewModel.getUiMode() == PreferencesRepository.UiMode.DARK)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.EnterFragment,
                R.id.RegisterFragment,
                R.id.WelcomeFragment,
                R.id.DrawerFragment
            ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.dark_cyan, theme))
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val firstFragment = fragment.childFragmentManager.fragments.lastOrNull()
        val secChild = (firstFragment?.childFragmentManager?.fragments?.lastOrNull()
                as? NavHostFragment)?.childFragmentManager?.fragments?.lastOrNull()

        if (firstFragment !is IOnBackPressed) {
            if (secChild !is IOnBackPressed) {
                super.onBackPressed()
                return
            }
            secChild.onBackPressed()
            return
        }
        firstFragment.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}