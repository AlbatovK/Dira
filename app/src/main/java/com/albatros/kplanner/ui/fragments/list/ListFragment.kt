package com.albatros.kplanner.ui.fragments.list

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ListFragmentBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.domain.extensions.playFadeOutAnimation
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.ui.activity.MainActivity
import com.albatros.kplanner.ui.adapter.note.NoteAdapter
import com.albatros.kplanner.ui.adapter.note.NoteAdapterListener
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding
    private val viewModel: ListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    var selecting = false
    var selected: MutableList<DiraNote> = mutableListOf()
    var selectedViews: MutableList<CardView> = mutableListOf()
    var defaultColor: Int = 0
    var selectingColor: Int = 0

    private val listener = object : NoteAdapterListener {
        override fun onNoteSelected(note: DiraNote, view: CardView) {}

        override fun onNoteClicked(note: DiraNote, view: CardView) {
            if (selecting) {
                if (selected.contains(note)) {
                    selected.remove(note)
                    selectedViews.remove(view)
                    view.setCardBackgroundColor(defaultColor)
                    if (selectedViews.isEmpty()) {
                        actionMode?.finish()
                    }
                    return
                }
                selected.add(note)
                selectedViews.add(view)
                view.setCardBackgroundColor(selectingColor)
                return
            }
            (activity as MainActivity).binding.toolbar.startActionMode(actionModeCallback)
            selected.add(note)
            selectedViews.add(view)
            selecting = true
            defaultColor = view.cardBackgroundColor.defaultColor

            view.setCardBackgroundColor(selectingColor)
        }
    }

    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            actionMode = mode
            selecting = true
            mode?.menuInflater?.inflate(R.menu.menu_action, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.action_add -> {
                    if (selected.isNotEmpty())
                        viewModel.addNotes(selected.toMutableList())

                    selectedViews.forEach { view ->
                        view.setCardBackgroundColor(
                            resources.getColor(
                                android.R.color.white,
                                context?.theme
                            )
                        )
                    }
                    mode?.finish()
                    true
                }
                else -> true
            }


        override fun onDestroyActionMode(mode: ActionMode?) {
            selectedViews.forEach { view -> view.setCardBackgroundColor(defaultColor) }
            selecting = false
            selected.clear()
            selectedViews.clear()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        selectingColor =
            MaterialColors.getColor(requireContext(), R.attr.SelectedItemBackground, Color.BLACK)

        viewModel.noteAdded.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.main_fragment)
            }
        }

        var f = binding.swipeRefreshLayout.javaClass.getDeclaredField("mCircleHeight")
        f.isAccessible = true
        f.setInt(binding.swipeRefreshLayout, 0)
        f = binding.swipeRefreshLayout.javaClass.getDeclaredField("mCircleWidth")
        f.isAccessible = true
        f.setInt(binding.swipeRefreshLayout, 0)

        val loaded = mutableListOf<DiraNote>()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.playFadeInAnimation(500L)
            viewModel.loadNextPage()
        }

        binding.notes.adapter = NoteAdapter(loaded, listener, false)

        binding.notes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.notes.observe(viewLifecycleOwner) {
            binding.progressBar.playFadeOutAnimation(500L)
            loaded.addAll(it)
            (binding.notes.adapter as NoteAdapter)
                .notifyItemRangeInserted((binding.notes.adapter as NoteAdapter).itemCount, it.size)
        }
    }
}