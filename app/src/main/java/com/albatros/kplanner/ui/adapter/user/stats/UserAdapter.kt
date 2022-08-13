package com.albatros.kplanner.ui.adapter.user.stats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.UserLayoutBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraUser

class UserAdapter(
    private val users: MutableList<DiraUser>,
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.user = users[position]
    }

    inner class UserViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

                    val text = root.context.getString(
                        R.string.place_str,
                        users.indexOf(user) + 1,
                        user.nickname
                    ) + if (users.indexOf(user) == 0) " \uD83D\uDC51" else ""

                    name.text = text

                    root.playFadeInAnimation(500L)
                }
            }
        }
    }
}