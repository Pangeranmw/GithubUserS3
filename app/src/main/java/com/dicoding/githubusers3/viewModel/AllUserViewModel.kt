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

class AllUsersViewModel : ViewModel() {
    private var listUsers = MutableLiveData<ArrayList<User>>()

    fun setAllUsers(context: Context) {
        ApiConfig.getService().getUsers().enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    listUsers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(context, "onFailure: ${response.message()}", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, "onFailure: ${t.message.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
    fun getAllUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

    companion object {
        const val TAG = "TAG"
    }
}