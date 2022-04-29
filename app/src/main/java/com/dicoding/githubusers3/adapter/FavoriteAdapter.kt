package com.dicoding.githubusers3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusers3.data.database.DataDao
import com.dicoding.githubusers3.data.database.DataRoom
import com.dicoding.githubusers3.data.database.DatabaseRoom
import com.dicoding.githubusers3.databinding.UserItemBinding
import com.dicoding.githubusers3.helper.FavoriteDiffCallback
import com.dicoding.githubusers3.ui.DetailActivity

class FavoriteAdapter(private val list: ArrayList<DataRoom>, private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var database: DataDao

    fun updateData(items: ArrayList<DataRoom>) {
        val diffCallback = FavoriteDiffCallback(list, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
    inner class ViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataRoom) {
            with(binding){
                tvName.text = data.username.toString()
                deleteUser.visibility = View.VISIBLE
                deleteUser.setOnClickListener{
                    database = DatabaseRoom.getDatabase(activity).dataDao()
                    Toast.makeText(
                        activity,
                        "${data.username} Berhasil dihapus dari favorit",
                        Toast.LENGTH_SHORT
                    ).show()
                    database.delete(data.username.toString())
                    updateData(database.getAllData() as ArrayList<DataRoom>)
                }
            }
            Glide.with(itemView)
                .load(data.avatar)
                .into(binding.imgUser)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, data.username.toString())
                activity.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
