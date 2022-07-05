package com.albatros.kplanner.ui.fragments.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.NotesList
import com.albatros.kplanner.model.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(private val api: DiraApi, private val repo: UserRepo) : ViewModel() {

    private val _notes: MutableLiveData<List<DiraNote>> = MutableLiveData<List<DiraNote>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = api.getAllNotes()
        }
    }

    val notes: LiveData<List<DiraNote>> = _notes

    private val _noteAdded: MutableLiveData<Boolean> = MutableLiveData(false)

    val noteAdded: LiveData<Boolean> = _noteAdded

    fun addNotes(notes: List<DiraNote>) {
        viewModelScope.launch(Dispatchers.Main) {
            _noteAdded.value = try {
                api.addNotes(NotesList(notes), repo.diraUser.tokenId)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}