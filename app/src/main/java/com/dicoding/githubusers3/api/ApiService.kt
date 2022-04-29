package com.dicoding.githubusers3.api

import com.dicoding.githubusers3.BuildConfig
import com.dicoding.githubusers3.data.model.User
import com.dicoding.githubusers3.data.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getUsers(): Call<ArrayList<User>>

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<UsersResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}