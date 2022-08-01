package com.albatros.kplanner.ui.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.auth.AuthUseCases
import com.albatros.kplanner.domain.usecase.autoenter.AutoEnterUseCases
import com.albatros.kplanner.domain.usecase.datatransfer.input.ServerInputUseCases
import com.albatros.kplanner.domain.usecase.note.AddNotesToScheduleByIdUseCase
import com.albatros.kplanner.domain.util.AuthResult
import com.albatros.kplanner.model.data.DiraUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authUseCases: AuthUseCases,
    private val autoEnterUseCases: AutoEnterUseCases,
    private val addNotesToScheduleById: AddNotesToScheduleByIdUseCase,
    private val serverInputUseCases: ServerInputUseCases
) : ViewModel() {

    private val _authResult: MutableLiveData<AuthResult> = MutableLiveData()
    val authResult: LiveData<AuthResult> = _authResult

    private val _diraUser: MutableLiveData<DiraUser?> = MutableLiveData()
    val diraUser: LiveData<DiraUser?> = _diraUser

    fun createInternalUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.Main) {
            _diraUser.value = try {
                val diraUser = serverInputUseCases.createUser(user)
                serverInputUseCases.createUsersSchedule()
                val welcomeNote = listOf(1L)
                addNotesToScheduleById(welcomeNote)
                diraUser
            } catch (e: Exception) {
                null
            }
        }
    }

    fun authenticate(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val authRes = authUseCases.signUpFirebase(email, password)
            _authResult.value = authRes
            if (authRes is AuthResult.AuthSuccess)
                autoEnterUseCases.setAutoEnterData(email, password)
        }
    }
}