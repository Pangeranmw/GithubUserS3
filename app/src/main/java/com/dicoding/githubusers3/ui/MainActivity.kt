package com.dicoding.githubusers3.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers3.R
import com.dicoding.githubusers3.databinding.ActivityMainBinding
import com.dicoding.githubusers3.viewModel.AllUsersViewModel
import com.dicoding.githubusers3.viewModel.SearchViewModel
import com.dicoding.githubusers3.adapter.UserAdapter
import com.dicoding.githubusers3.data.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private val allUserViewModel: AllUsersViewModel by viewModels()
    private val searchUserModel: SearchViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = " ${getString(R.string.app_name)}"
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(R.drawable.ic_github_logo_white)
        }

        getUser()
        showRecyclerList()
    }

    private fun getUser() {
        allUserViewModel.setAllUsers(this)
        loading(true)
        allUserViewModel.getAllUsers().observe(this) { listUsers ->
            loading(false)
            if (listUsers != null) {
                adapter.setData(listUsers)
                showNotFound(false)
            }else {
                showNotFound(true)
            }
        }
    }
    private fun getUserSearch(id: String) {
        loading(true)
        searchUserModel.setSearchUsers(id, this)
        searchUserModel.getSearchUsers().observe(this) { listSearchUsers ->
            loading(false)
            if (listSearchUsers != null) {
                showNotFound(false)
                adapter.setData(listSearchUsers)
            } else {
                showNotFound(true)
            }
        }
    }

    private fun loading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showRecyclerList() {
        adapter = UserAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showNotFound(state: Boolean) {
        binding.apply{
            if (state){
                tvNotFound.visibility = View.VISIBLE
                rvUser.visibility = View.GONE
            } else{
                tvNotFound.visibility = View.GONE
                rvUser.visibility = View.VISIBLE
            }
        }
    }

    private fun showSelectedUser(data: User) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, data.login)

        this@MainActivity.startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = item.actionView as SearchView
                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.find)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        if (query.isNotEmpty()) {
                            getUserSearch(query)
                        }
                        return true
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })
                return true
            }
            R.id.favorite_user -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_refresh -> {
                getUser()
                return true
            }
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.settings -> {
                val intentSetting = Intent(this, SettingActivity::class.java)
                startActivity(intentSetting)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}