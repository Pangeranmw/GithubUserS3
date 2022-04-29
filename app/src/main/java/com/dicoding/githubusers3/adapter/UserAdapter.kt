package com.dicoding.githubusers3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubusers3.data.model.User
import com.dicoding.githubusers3.databinding.UserItemBinding


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mData = ArrayList<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    inner class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: User) {
            Glide.with(itemView.context)
                .load(users.avatar_url)
                .apply(
                    RequestOptions()
                        .fitCenter()
                        .format(com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888)
                        .override(250, 250)
                )
                .into(binding.imgUser)
            binding.tvName.text = users.login
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Kamu memilih ${users.login}",
                    Toast.LENGTH_SHORT
                ).show()

                onItemClickCallback?.onItemClicked(users)
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}