package com.albatros.kplanner.ui.fragments.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.NavigationFragmentBinding
import com.albatros.kplanner.ui.activity.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationFragment : Fragment(), MainActivity.IOnBackPressed {

    override fun onBackPressed(): Boolean {
        activity?.finish()
        activity?.finishAffinity()
        return true
    }

    private val viewModel: NavigationViewModel by viewModel()
    private lateinit var binding: NavigationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = NavigationFragmentBinding.inflate(inflater, container, false)
        val navHostFragment: View = binding.container.findViewById(R.id.bottom_nav_host_fragment)
        val navController = Navigation.findNavController(navHostFragment)
        val appBarConfiguration = AppBarConfiguration
            .Builder(R.id.main_fragment, R.id.list_fragment, R.id.users_fragment, R.id.stats_fragment).setOpenableLayout((activity as MainActivity).binding.drawerLayout).build()
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupActionBarWithNavController(activity as MainActivity, navController, appBarConfiguration)
        return binding.root
    }
}