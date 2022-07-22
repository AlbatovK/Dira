package com.albatros.kplanner.ui.fragments.drawer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.DrawerFragmentBinding
import com.albatros.kplanner.ui.activity.MainActivity

class DrawerFragment : Fragment() {

    private lateinit var binding: DrawerFragmentBinding
   private lateinit var controller: NavController

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((activity as MainActivity).binding.drawerLayout.isDrawerOpen((activity as MainActivity).binding.drawerLayout))
            return true
        (activity as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawerFragmentBinding.inflate(inflater, container, false)
        val appBarConfig = AppBarConfiguration
            .Builder(R.id.main_fragment, R.id.NavigationFragment,
                R.id.list_fragment, R.id.users_fragment, R.id.stats_fragment).setOpenableLayout((activity as MainActivity).binding.drawerLayout).build()
        val navHostFragment: View = binding.root.findViewById(R.id.drawer_nav_host_fragment)
        controller = Navigation.findNavController(navHostFragment)
        NavigationUI.setupActionBarWithNavController(activity as MainActivity, controller, appBarConfig)
        NavigationUI.setupWithNavController((activity as MainActivity).binding.drawerNavView, controller)
        (activity as MainActivity).binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        (activity as MainActivity).binding.toolbar.setNavigationOnClickListener {
            if (!(activity as MainActivity).binding.drawerLayout.isDrawerOpen(GravityCompat.START))
                    (activity as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return binding.root
    }

}