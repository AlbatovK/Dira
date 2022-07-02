package com.albatros.kplanner.ui.fragments.enter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.isEntryValid
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.util.EnterResult
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.repo.UserRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EnterViewModel(private val api: DiraApi, private val repo: UserRepo) : ViewModel() {

    private val _userEntryLive: MutableLiveData<EnterResult> = MutableLiveData()
    val userEntryLive: LiveData<EnterResult> = _userEntryLive

    private val _diraUser: MutableLiveData<DiraUser?> = MutableLiveData()
    val diraUser: LiveData<DiraUser?> = _diraUser

    init {
        viewModelScope.launch(Dispatchers.Main) {
            try { api.preActivate() } catch (e: Exception) {}
        }
    }

    fun transformDiraUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.Main) {
            _diraUser.value = try {
                val apiUser: DiraUser = api.findUserByToken(user.uid)
                repo.diraUser = apiUser.copy()
                repo.schedule = api.getScheduleByOwnerId(apiUser.tokenId)
                apiUser
            } catch (e: Exception) { null }
        }
    }

    fun authenticate(email: String, password: String) {
        if (!isEntryValid(email, password)) {
            _userEntryLive.value = EnterResult.EntryInvalid
            return
        }

        _userEntryLive.value = EnterResult.EntryStarted
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _userEntryLive.value = EnterResult.EntrySuccess(it.user!!)
        }.addOnFailureListener {
            _userEntryLive.value = EnterResult.EntryFailure(it)
        }
    }
}