package com.albatros.kplanner.ui.activity

import androidx.lifecycle.ViewModel
import com.albatros.kplanner.domain.usecase.uimode.GetUiModeUseCase
import com.albatros.kplanner.model.repo.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

class MainActivityViewModel(
    private val getUiModeUseCase: GetUiModeUseCase,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    fun getUiMode() = getUiModeUseCase()

    private var lastConnectivityState: ConnectivityRepository.ConnectionStatus? = null

    val networkStatusFlow: Flow<ConnectivityRepository.ConnectionStatus> =
        connectivityRepository.getStatus().distinctUntilChanged().filter { status ->
            val result =
                !(lastConnectivityState == null
                        && status == ConnectivityRepository.ConnectionStatus.Available)
            return@filter result.also { lastConnectivityState = status }
        }.distinctUntilChanged()

}