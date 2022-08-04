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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListViewModel(
    private val notesUseCases: NotesUseCases
) : ViewModel() {

    private val _notes: MutableLiveData<List<DiraNote>> = MutableLiveData<List<DiraNote>>()

    val notes: LiveData<List<DiraNote>> = _notes

    private val pageSize = 10
    private var key = 0
    private var isLoading = false

    init {
        loadNextPage()
        isLoading = true
    }

    fun loadNextPage() {
        if (isLoading) {
            return
        }
        viewModelScope.launch {
            isLoading = true
            _notes.value = notesUseCases.loadNotes(key * pageSize, ++key * pageSize)
            isLoading = false
        }
    }

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