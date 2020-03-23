/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.api

import com.example.cataloguemovie2.data.model.MovieDetails
import com.example.cataloguemovie2.data.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
* https://api.themoviedb.org/3/movie/181812?api_key=ba923ef570e2fda6a523a4ff8aaa7fac
* https://api.themoviedb.org/3/movie/popular?api_key=ba923ef570e2fda6a523a4ff8aaa7fac&page=1
* https://api.themoviedb.org/3/
*/

interface TheMovieDBInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page")page: Int): Single<MovieResponse>

}