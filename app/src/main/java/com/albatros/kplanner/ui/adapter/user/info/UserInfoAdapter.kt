package com.albatros.kplanner.ui.adapter.user.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.UserInfoBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraUser

class UserInfoAdapter(
    private val owner: DiraUser,
    private val listener: UserAdapterListener,
    private val isSkeleton: Boolean,
) : ListAdapter<DiraUser, UserInfoAdapter.UserViewHolder>(diffCallback) {

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<DiraUser>() {

            override fun areItemsTheSame(oldItem: DiraUser, newItem: DiraUser): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: DiraUser, newItem: DiraUser): Boolean = oldItem.tokenId == newItem.tokenId
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.user = currentList[position]
    }

    inner class UserViewHolder(private val binding: UserInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var user: DiraUser? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(user: DiraUser?) {
            if (isSkeleton)
                return
            user?.let {
                with(binding) {

                    points.text = root.context.getString(
                        R.string.points_template_str,
                        user.scoreOfWeek
                    )

                    name.text = root.context.getString(
                        R.string.place_str,
                        currentList.indexOf(user) + 1,
                        user.nickname
                    )

                    var isFriend = owner.friendsIds.contains(user.tokenId)
                    image.setImageResource(
                        if (isFriend) R.drawable.ic_account else R.drawable.ic_add_friend
                    )

                    image.setOnClickListener {
                        if (isFriend) {
                            listener.onFriendClicked(user, cardView)
                        } else {
                            listener.onUserClicked(user, image)
                            isFriend = true
                        }
                    }

                    root.playFadeInAnimation(500L)
                }
            }
        }
    }
}