package com.dicoding.githubusers3.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubusers3.api.ApiConfig
import com.dicoding.githubusers3.data.model.User
import com.dicoding.githubusers3.data.model.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<ArrayList<User>>()
    private val listUsers: LiveData<ArrayList<User>> = _listUsers

    fun setSearchUsers(username: String, context: Context) {
        ApiConfig.getService().getSearchUsers(username).enqueue(object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                if (response.isSuccessful) {
                    _listUsers.postValue(response.body()?.items)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(context, "onFailure: ${response.message()}", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, "onFailure: ${t.message.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> = listUsers

    companion object {
        const val TAG = "TAG"
    }
}