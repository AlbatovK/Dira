package com.albatros.kplanner.ui.adapter.user.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.UserInfoBinding
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.repo.UserRepo

class UserInfoAdapter(
    private val users: MutableList<DiraUser>,
    private val owner: DiraUser,
    private val listener: UserAdapterListener
) : RecyclerView.Adapter<UserInfoAdapter.UserViewHolder>() {

    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) { holder.user = users[position] }

    inner class UserViewHolder(private val binding: UserInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        var user: DiraUser? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(user: DiraUser?) {
            user?.let {
                with(binding) {
                    points.text = root.context.getString(
                        R.string.points_template_str,
                        user.scoreOfWeek
                    )
                    name.text = root.context.getString(
                        R.string.place_str,
                        users.indexOf(user) + 1,
                        user.nickname
                    )

                    var isFriend = owner.friendsIds.contains(user.tokenId)
                    binding.image.setImageResource(
                        if (isFriend) R.drawable.ic_account else R.drawable.ic_add_friend
                    )

                    binding.image.setOnClickListener {
                        if (isFriend) {
                            listener.onFriendClicked(user, binding.cardView)
                        } else {
                            listener.onUserClicked(user, binding.image)
                            isFriend = true
                        }
                    }

                    root.playFadeInAnimation(500L)
                }
            }
        }
    }
}