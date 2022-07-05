package com.albatros.kplanner.ui.fragments.list

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ListFragmentBinding
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.ui.activity.MainActivity
import com.albatros.kplanner.ui.adapter.note.NoteAdapter
import com.albatros.kplanner.ui.adapter.note.NoteAdapterListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val listener = object: NoteAdapterListener {
        override fun onNoteSelected(note: DiraNote, view: CardView) {
            if (selecting)
                    return
            (activity as MainActivity).binding.toolbar.startActionMode(actionModeCallback)
            selected.add(note)
            selectedViews.add(view)
            view.setCardBackgroundColor(resources.getColor(android.R.color.darker_gray, context?.theme))
        }

        override fun onNoteClicked(note: DiraNote, view: CardView) {
            if (selecting) {
                if (selected.contains(note)) {
                    selected.remove(note)
                    selectedViews.remove(view)
                    view.setCardBackgroundColor(resources.getColor(android.R.color.white, context?.theme))
                    return
                }
                selected.add(note)
                selectedViews.add(view)
                view.setCardBackgroundColor(resources.getColor(android.R.color.darker_gray, context?.theme))
            }
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            selecting = true
            mode?.menuInflater?.inflate(R.menu.menu_action, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = when(item?.itemId) {
            android.R.id.home -> {
                val direction = ListFragmentDirections.actionListFragmentToMainFragment()
                findNavController().navigate(direction)
                true
            }
            R.id.action_add -> {
                if (selected.isNotEmpty())
                    viewModel.addNotes(selected.toMutableList())

                selectedViews.forEach {
                    view -> view.setCardBackgroundColor(resources.getColor(android.R.color.white, context?.theme))
                }
                mode?.finish()
                true
            }
            else -> true
        }


        override fun onDestroyActionMode(mode: ActionMode?) {
            selecting = false
            selected.clear()
            selectedViews.forEach {
                    view -> view.setCardBackgroundColor(resources.getColor(android.R.color.white, context?.theme))
            }
            selectedViews.clear()
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.noteAdded.observe(viewLifecycleOwner) {
            if (it) {
                val direction = ListFragmentDirections.actionListFragmentToMainFragment()
                findNavController().navigate(direction)
            }
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            binding.list.adapter = NoteAdapter(it.toMutableList(), listener)
            binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}