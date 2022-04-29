package com.dicoding.githubusers3.adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubusers3.ui.FollowersFollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = " "

    override fun createFragment(position: Int): Fragment {
        return FollowersFollowingFragment.newInstance(position, username)
    }

    override fun getItemCount(): Int {
        return 2
    }
}