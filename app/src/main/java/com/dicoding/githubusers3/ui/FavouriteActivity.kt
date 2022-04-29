package com.dicoding.githubusers3.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers3.adapter.FavoriteAdapter
import com.dicoding.githubusers3.data.database.DataRoom
import com.dicoding.githubusers3.databinding.ActivityFavouriteBinding
import com.dicoding.githubusers3.viewModel.FavoriteViewModel

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Favorite User"

        loading(true)
    }

    private fun loading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setData(list: ArrayList<DataRoom>) {
        favoriteAdapter = FavoriteAdapter(list, this)
        binding.rvFavourite.layoutManager = LinearLayoutManager(this)
        binding.rvFavourite.adapter = favoriteAdapter
    }

    override fun onResume() {
        super.onResume()
        showFavourite()
    }
    private fun showFavourite() {
        favoriteViewModel.getDataFavorite(this).observe(this) { listUsers ->
            loading(false)
            if (listUsers.size != 0) {
                setData(listUsers)
                showNoFavourite(false)
            } else{
                setData(listUsers)
                showNoFavourite(true)
            }
        }
    }
    fun showNoFavourite(state: Boolean) {
        binding.tvNoFavourite.visibility = if (state) View.VISIBLE else View.GONE
    }
}