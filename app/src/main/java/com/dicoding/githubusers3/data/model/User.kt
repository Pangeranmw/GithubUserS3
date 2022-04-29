package com.dicoding.githubusers3.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String?=null,
    var login: String?=null,
    var avatar_url: String?=null,
    var company: String?=null,
    var location: String?=null,
    var public_repos: Int?=null,
    var followers: Int?=null,
    var following: Int?=null,
) : Parcelable
