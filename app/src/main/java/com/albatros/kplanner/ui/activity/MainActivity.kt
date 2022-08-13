package com.albatros.kplanner.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ActivityMainBinding
import com.albatros.kplanner.model.repo.ConnectivityRepository
import com.albatros.kplanner.model.repo.PreferencesRepository
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
                R.id.WelcomeFragment,
                R.id.DrawerFragment
            ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.toolbar.setTitleTextColor(
            MaterialColors.getColor(
                this,
                R.attr.AppBarTextColor,
                Color.WHITE
            )
        )

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.networkStatusFlow.collectLatest {
                    when (it) {
                        ConnectivityRepository.ConnectionStatus.Unavailable,
                        ConnectivityRepository.ConnectionStatus.Lost -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Отсутствует подключение к интернету. " +
                                        "Некоторые фукции приложения могут работать неправильно.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        ConnectivityRepository.ConnectionStatus.Available -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Подключение восстановлено.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
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