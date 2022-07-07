package com.albatros.kplanner.ui.fragments.users

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.TypedValue
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ListFragmentBinding
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.ui.activity.MainActivity
import com.albatros.kplanner.ui.adapter.user.info.UserAdapterListener
import com.albatros.kplanner.ui.adapter.user.info.UserInfoAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class UsersListFragment : Fragment(), MainActivity.IOnBackPressed {

    private lateinit var binding: ListFragmentBinding
    private val viewModel: UsersListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        val direction = UsersListFragmentDirections.actionUsersListFragmentToMainFragment()
        findNavController().navigate(direction)
        return true
    }

    private val listener = object : UserAdapterListener {
        override fun onFriendClicked(user: DiraUser, view: View) {
            val direction = UsersListFragmentDirections.actionUserListFragmentToProfileFragment(user)
            findNavController().navigate(direction)
        }

        override fun onUserClicked(user: DiraUser, view: ImageView) {
            viewModel.addFriend(user)
            view.setImageResource(R.drawable.ic_account)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_no_filter -> {
            viewModel.loadUsersList()
            true
        }
        R.id.action_filter_friends -> {
            viewModel.loadFriends()
            true
        }
        android.R.id.home -> {
            val direction = UsersListFragmentDirections.actionUsersListFragmentToMainFragment()
            findNavController().navigate(direction)
            true
        } else -> true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_list_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = "Введите имя"
        searchView.setIconifiedByDefault(false)

        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(R.color.dark_cyan, context?.theme))
        searchEditText.setHintTextColor(resources.getColor(R.color.dark_cyan, context?.theme))
        searchEditText.setBackgroundResource(R.color.light_cyan)

        val listener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.loadUsersList()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true
        }

        menu.findItem(R.id.action_search).setOnActionExpandListener(listener)

        searchView.setOnCloseListener {
            viewModel.loadUsersList()
            true
        }

        val queryListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.trim()?.lowercase()?.let { viewModel.fetchByTopics(query) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }

        searchView.setOnQueryTextListener(queryListener)
        val manager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val info = manager.getSearchableInfo(activity?.componentName)
        searchView.setSearchableInfo(info)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.users.observe(viewLifecycleOwner) {
            binding.list.adapter = UserInfoAdapter(it.toMutableList(), viewModel.getUser(), listener)
            binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}