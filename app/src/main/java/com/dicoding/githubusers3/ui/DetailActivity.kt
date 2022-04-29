package com.dicoding.githubusers3.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.githubusers3.R
import com.dicoding.githubusers3.adapter.SectionPagerAdapter
import com.dicoding.githubusers3.data.database.DataDao
import com.dicoding.githubusers3.data.database.DataRoom
import com.dicoding.githubusers3.data.database.DatabaseRoom
import com.dicoding.githubusers3.data.model.User
import com.dicoding.githubusers3.databinding.ActivityUserDetailBinding
import com.dicoding.githubusers3.viewModel.DetailUserViewModel
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val getUserModel: DetailUserViewModel by viewModels()
    private lateinit var database: DataDao
    private var username = ""
    private var avatar = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_DATA).toString()

        viewPagerConfig()

        showDataUser(username)

        database = DatabaseRoom.getDatabase(applicationContext).dataDao()
        val isFavoriteExist = database.dataExist(username)
        setIcon(isFavoriteExist)

        binding.favorite.setOnClickListener{
            database = DatabaseRoom.getDatabase(applicationContext).dataDao()
            if (!database.dataExist(username)) {
                setIcon(true)
                Toast.makeText(
                    this,
                    "$username ${getString(R.string.succes_add)}",
                    Toast.LENGTH_SHORT
                ).show()
                val newData = DataRoom(
                    username = username,
                    avatar = avatar,
                )
                database.insert(newData)
            } else {
                setIcon(false)
                Toast.makeText(
                    this,
                    "$username ${getString(R.string.success_delete)}",
                    Toast.LENGTH_SHORT
                ).show()
                database.delete(username)
            }
        }
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) {
                tab, position ->tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        sectionsPagerAdapter.username = username
    }

    private fun setActionBarTitle(title: String) {
        val user = title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        if (supportActionBar != null) {
            if (Locale.getDefault().language.equals("en")) this.title = "$user's ${getString(R.string.profile)}"
            else this.title = "${getString(R.string.profile)} $user"
        }
    }

    private fun setData(listUser: User) {
        avatar = listUser.avatar_url.toString()
        listUser.login?.let { setActionBarTitle(it) }

        binding.apply {
            detailUsername.text = listUser.login
            detailName.text = listUser.name
            detailLocation.text = listUser.location
            detailRepo.text = listUser.public_repos.toString()
            detailCompany.text = listUser.company
            detailFollowers.text = listUser.followers.toString()
            detailFollowing.text = listUser.following.toString()
        }
        Glide.with(this)
            .load(listUser.avatar_url)
            .into(binding.detailAvatar)
    }

    private fun showDataUser(username: String) {
        getUserModel.setUserDetail(username, this)
        getUserModel.getData().observe(this) { userData ->
            if (userData != null) {
                Log.d("tag", userData.toString())
                setData(userData)
            }
        }
    }
    private fun setIcon(exist: Boolean) {
        binding.favorite.setImageDrawable(
            if (exist) {
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_baseline_favorite_24
                )
            }else {
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_favorite_border_24
                    )
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        menu.findItem(R.id.search).isVisible = false
        menu.findItem(R.id.menu_refresh).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.favorite_user -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
            R.id.settings -> {
                val intentSetting = Intent(this, SettingActivity::class.java)
                startActivity(intentSetting)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_DATA = "extra_data"
    }
}