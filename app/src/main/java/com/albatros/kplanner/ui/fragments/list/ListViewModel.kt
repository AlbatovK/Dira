package com.albatros.kplanner.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.note.AddNotesToScheduleUseCase
import com.albatros.kplanner.domain.usecase.note.LoadAllNotesUseCase
import com.albatros.kplanner.domain.usecase.note.NotesUseCases
import com.albatros.kplanner.model.data.DiraNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(
    private val notesUseCases: NotesUseCases
) : ViewModel() {

    private val _notes: MutableLiveData<List<DiraNote>> = MutableLiveData<List<DiraNote>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = notesUseCases.loadAllNotes()
        }
    }

    val notes: LiveData<List<DiraNote>> = _notes

    private val _noteAdded: MutableLiveData<Boolean> = MutableLiveData(false)

    val noteAdded: LiveData<Boolean> = _noteAdded

    fun addNotes(notes: List<DiraNote>) {
        viewModelScope.launch(Dispatchers.Main) {
            _noteAdded.value = try {
                notesUseCases.addNotesToSchedule(notes)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}