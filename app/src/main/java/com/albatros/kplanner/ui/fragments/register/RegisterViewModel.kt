package com.albatros.kplanner.ui.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.auth.AuthUseCases
import com.albatros.kplanner.domain.usecase.autoenter.AutoEnterUseCases
import com.albatros.kplanner.domain.usecase.datatransfer.input.ServerInputUseCases
import com.albatros.kplanner.domain.usecase.note.AddNotesToScheduleByIdUseCase
import com.albatros.kplanner.domain.util.AuthResult
import com.albatros.kplanner.model.data.DiraUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authUseCases: AuthUseCases,
    private val autoEnterUseCases: AutoEnterUseCases,
    private val addNotesToScheduleById: AddNotesToScheduleByIdUseCase,
    private val serverInputUseCases: ServerInputUseCases
) : ViewModel() {

    private val _eventChannel = Channel<UiEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    sealed class UiEvent {
        data class OnAuthStateChanged(val authResult: AuthResult) : UiEvent()
        data class OnUserFetchCompleted(val user: DiraUser?) : UiEvent()
    }

    fun startAuth(email: String, password: String) {
        viewModelScope.launch {
            val authRes = authUseCases.signUpFirebase(email, password)

            _eventChannel.send(
                UiEvent.OnAuthStateChanged(authRes)
            )

            if (authRes is AuthResult.AuthSuccess) {
                autoEnterUseCases.setAutoEnterData(email, password)

                var user: DiraUser?

                try {
                    user = serverInputUseCases.createUser(authRes.user)
                    serverInputUseCases.createUsersSchedule()
                    val welcomeNote = listOf(1L)
                    addNotesToScheduleById(welcomeNote)
                } catch (e: Exception) {
                    user = null
                }

                _eventChannel.send(
                    UiEvent.OnUserFetchCompleted(user)
                )
            }
        }
    }
}