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

class DetailUserViewModel : ViewModel(){
    private val _userDetail = MutableLiveData<User>()
    private val userDetail: LiveData<User> = _userDetail

    fun setUserDetail(username: String, context: Context) {
        ApiConfig.getService().getDetailUsers(username).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, "onFailure: ${t.message.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun getData(): LiveData<User> = userDetail

    companion object {
        const val TAG = "TAG"
    }
}