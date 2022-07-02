package com.albatros.kplanner.ui.fragments.list

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kplanner.databinding.ListFragmentBinding
import com.albatros.kplanner.model.data.DiraNote
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

    private val listener = object: NoteAdapterListener {
        override fun onNoteSelected(note: DiraNote, view: View) {
            viewModel.addNote(note)
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