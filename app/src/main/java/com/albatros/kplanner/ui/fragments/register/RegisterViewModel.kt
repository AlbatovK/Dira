package com.albatros.kplanner.ui.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.isEntryValid
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.util.EnterResult
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.data.NotesIdsList
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.repo.PreferencesRepo
import com.albatros.kplanner.model.repo.UserRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val api: DiraApi, private val repo: UserRepo, private val prefRepo: PreferencesRepo) : ViewModel() {

    private val _user: MutableLiveData<EnterResult> = MutableLiveData()
    val user: LiveData<EnterResult> = _user

    private val _diraUser: MutableLiveData<DiraUser?> = MutableLiveData()
    val diraUser: LiveData<DiraUser?> = _diraUser

    fun transformDiraUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.Main) {
            _diraUser.value = try {
                val diraUser = DiraUser(user.uid, 0, user.email!!, user.email!!.split("@")[0])
                val apiUser = api.createUser(diraUser)
                repo.diraUser = apiUser.copy()
                repo.schedule = Schedule(ownerId = apiUser.tokenId)
                api.createSchedule(repo.schedule)
                api.addNotes(NotesIdsList(listOf(1L)), apiUser.tokenId) // Hardcoded note
                apiUser
            } catch (e: Exception) {
                null
            }
        }
    }

    fun authenticate(email: String, password: String) {
        if (!isEntryValid(email, password)) {
            _user.value = EnterResult.EntryInvalid
            return
        }

        _user.value = EnterResult.EntryStarted
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _user.value = EnterResult.EntrySuccess(it.user!!)
            prefRepo.setEmail(email)
            prefRepo.setPassword(password)
        }.addOnFailureListener {
            _user.value = EnterResult.EntryFailure(it)
        }
    }
}