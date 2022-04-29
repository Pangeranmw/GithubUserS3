package com.dicoding.githubusers3.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers3.R
import com.dicoding.githubusers3.adapter.UserAdapter
import com.dicoding.githubusers3.data.model.User
import com.dicoding.githubusers3.databinding.FragmentFollowersFollowingBinding
import com.dicoding.githubusers3.viewModel.FollowersViewModel
import com.dicoding.githubusers3.viewModel.FollowingViewModel


class FollowersFollowingFragment : Fragment() {
    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private val getFollowersModel: FollowersViewModel by viewModels()
    private val getFollowingModel: FollowingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()

        binding.rvUserFollowerFollowing.layoutManager = LinearLayoutManager(this.context)
        binding.rvUserFollowerFollowing.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(USERNAME)
        if (index == 0) {
            showFollowers(username.toString())
        } else {
            showFollowing(username.toString())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showSelectedUser(data: User) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, data.login)
        startActivity(intent)
    }
    private fun showFollowers(username: String) {
        loading(true)
        getFollowersModel.setData(username, requireContext())
        getFollowersModel.getData().observe(viewLifecycleOwner) { listUsers ->
            loading(false)
            if (listUsers.size != 0) {
                adapter.setData(listUsers)
            } else {
                binding.tvNoFollowersFollowing.visibility = View.VISIBLE
                binding.tvNoFollowersFollowing.text = R.string.no_followers.toString()
            }
        }
    }

    private fun showFollowing(username: String) {
        loading(true)
        getFollowingModel.setData(username, requireContext())
        getFollowingModel.getData().observe(viewLifecycleOwner) { listUsers ->
            loading(false)
            if (listUsers.size != 0) {
                adapter.setData(listUsers)
            } else {
                binding.tvNoFollowersFollowing.text = R.string.no_following.toString()
                binding.tvNoFollowersFollowing.visibility = View.VISIBLE
            }
        }
    }

    private fun loading(isLoading: Boolean){
        binding.progressBarFollowingFollowers.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val USERNAME = "section_username"

        @JvmStatic
        fun newInstance(index: Int, username: String) = FollowersFollowingFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putString(USERNAME, username)
            }
        }
    }
}