package com.dicoding.githubusers3.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubusers3.api.ApiConfig
import com.dicoding.githubusers3.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _followingModel = MutableLiveData<ArrayList<User>>()
    private val followingModel: LiveData<ArrayList<User>> = _followingModel

    fun setData(username: String, context: Context) {
        ApiConfig.getService().getFollowing(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    _followingModel.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, "onFailure: ${t.message.toString()}", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    fun getData(): LiveData<ArrayList<User>> = followingModel

    companion object {
        const val TAG = "TAG"
    }
}