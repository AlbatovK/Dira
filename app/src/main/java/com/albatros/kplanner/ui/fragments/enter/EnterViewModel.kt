package com.albatros.kplanner.ui.fragments.enter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.auth.AuthUseCases
import com.albatros.kplanner.domain.usecase.autoenter.AutoEnterUseCases
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.InternalDataFetchUseCases
import com.albatros.kplanner.domain.util.AuthResult
import com.albatros.kplanner.model.data.DiraUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EnterViewModel(
    private val autoEnterUseCases: AutoEnterUseCases,
    private val authUseCases: AuthUseCases,
    private val internalDataFetchUseCases: InternalDataFetchUseCases
) : ViewModel() {

    private val _eventChannel = Channel<UiEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    sealed class UiEvent {
        data class OnAuthStateChanged(val authResult: AuthResult) : UiEvent()
        data class OnUserFetchCompleted(val user: DiraUser?) : UiEvent()
    }

    val lastEnterData = liveData {
        emit(autoEnterUseCases.getAutoEnterData())
    }

    fun startAuth(email: String, password: String) {
        viewModelScope.launch {
            val authRes = authUseCases.signInFirebase(email, password)

            _eventChannel.send(
                UiEvent.OnAuthStateChanged(authRes)
            )

            if (authRes is AuthResult.AuthSuccess) {
                autoEnterUseCases.setAutoEnterData(email, password)

                var user: DiraUser?

                try {
                    user = internalDataFetchUseCases.loadUser(authRes.user.uid)
                    internalDataFetchUseCases.loadUsersSchedule(authRes.user.uid)
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