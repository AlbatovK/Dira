package com.albatros.kplanner.ui.fragments.enter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.auth.AuthUseCases
import com.albatros.kplanner.domain.usecase.autoenter.AutoEnterUseCases
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.InternalDataFetchUseCases
import com.albatros.kplanner.domain.util.AuthResult
import com.albatros.kplanner.model.data.DiraUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EnterViewModel(
    private val autoEnterUseCases: AutoEnterUseCases,
    private val authUseCases: AuthUseCases,
    private val internalDataFetchUseCases: InternalDataFetchUseCases
) : ViewModel() {

    private val _authResult: MutableLiveData<AuthResult> = MutableLiveData()
    val authResult: LiveData<AuthResult> = _authResult

    private val _diraUser: MutableLiveData<DiraUser?> = MutableLiveData()
    val diraUser: LiveData<DiraUser?> = _diraUser

    private val _lastEnterData = MutableLiveData<Pair<String, String>>().apply {
        value = autoEnterUseCases.getAutoEnterData()
    }

    val lastEnterData: LiveData<Pair<String, String>> = _lastEnterData

    fun authenticate(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val authRes = authUseCases.signInFirebase(email, password)
            _authResult.value = authRes
            if (authRes is AuthResult.AuthSuccess)
                autoEnterUseCases.setAutoEnterData(email, password)
        }
    }

    fun loadInternalUser(firebaseUser: FirebaseUser) {
        viewModelScope.launch(Dispatchers.Main) {
            _diraUser.value = try {
                val user = internalDataFetchUseCases.loadUser(firebaseUser.uid)
                internalDataFetchUseCases.loadUsersSchedule(firebaseUser.uid)
                user
            } catch (e: Exception) {
                null
            }
        }
    }
}