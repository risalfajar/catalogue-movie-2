/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.model


import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String
)