package com.albatros.kplanner.ui.fragments.drawer

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.DrawerFragmentBinding
import com.albatros.kplanner.ui.activity.MainActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrawerFragment : Fragment() {

    private val viewModel: DrawerViewModel by viewModel()
    private lateinit var binding: DrawerFragmentBinding

    private lateinit var controller: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private fun openDrawerIfClosed() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        openDrawerIfClosed()
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navHostFragment: View = binding.root.findViewById(R.id.drawer_nav_host_fragment)
        controller = Navigation.findNavController(navHostFragment)
        drawerLayout = (activity as MainActivity).binding.drawerLayout
        navView = (activity as MainActivity).binding.drawerNavView
        val mainActivity = activity as MainActivity

        val appBarConfig = AppBarConfiguration.Builder(R.id.main_fragment, R.id.NavigationFragment,
            R.id.list_fragment, R.id.users_fragment, R.id.stats_fragment
        ).setOpenableLayout(drawerLayout).build()

        NavigationUI.setupActionBarWithNavController(mainActivity, controller, appBarConfig)
        NavigationUI.setupWithNavController(navView, controller)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        mainActivity.binding.toolbar.setNavigationOnClickListener {
            openDrawerIfClosed()
        }

        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_exit) {
                activity?.finishAffinity()
                true
            } else {
                drawerLayout.closeDrawer(GravityCompat.START)
                lifecycleScope.launch {
                    delay(200)
                    NavigationUI.onNavDestinationSelected(it, controller)
                }
                true
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFlow.collectLatest {
                    val header = navView.getHeaderView(0)
                    header.findViewById<TextView>(R.id.nickname_txt).text = it.nickname
                    header.findViewById<TextView>(R.id.email_txt).text = it.email
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}