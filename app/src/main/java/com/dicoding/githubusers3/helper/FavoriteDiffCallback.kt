package com.dicoding.githubusers3.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubusers3.data.database.DataRoom

class FavoriteDiffCallback(private val mOldFavList: ArrayList<DataRoom>, private val mNewFavList: ArrayList<DataRoom>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavList[oldItemPosition]
        val newFavorite = mNewFavList[newItemPosition]
        return oldFavorite.name == newFavorite.name && oldFavorite.username == newFavorite.username
    }
}