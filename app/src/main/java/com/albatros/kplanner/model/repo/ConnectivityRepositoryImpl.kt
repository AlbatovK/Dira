package com.albatros.kplanner.model.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class ConnectivityRepositoryImpl(context: Context) : ConnectivityRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun getStatus(): Flow<ConnectivityRepository.ConnectionStatus> = callbackFlow {

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch {
                    send(ConnectivityRepository.ConnectionStatus.Available)
                }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch {
                    send(ConnectivityRepository.ConnectionStatus.Losing)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch {
                    send(ConnectivityRepository.ConnectionStatus.Lost)
                }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch {
                    send(ConnectivityRepository.ConnectionStatus.Unavailable)
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}