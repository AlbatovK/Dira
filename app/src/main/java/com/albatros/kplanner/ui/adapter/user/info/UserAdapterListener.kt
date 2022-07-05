package com.albatros.kplanner.ui.adapter.user.info

import android.view.View
import android.widget.ImageView
import com.albatros.kplanner.model.data.DiraUser

interface UserAdapterListener {

    fun onFriendClicked(user: DiraUser, view: View)

    fun onUserClicked(user: DiraUser, view: ImageView)
}