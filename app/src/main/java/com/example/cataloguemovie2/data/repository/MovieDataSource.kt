/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.cataloguemovie2.data.api.FIRST_PAGE
import com.example.cataloguemovie2.data.api.TheMovieDBInterface
import com.example.cataloguemovie2.data.model.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//load data based on page number
class MovieDataSource (private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    //load initial data (request the first page)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.movieList, null, ++page)
                    networkState.postValue(NetworkState.LOADED)
                },{
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource", it.message ?: "Unknown error")
                })
        )
    }

    //load the next page, will be called when user scrolls down
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //if we have more pages to load
                    if(it.totalPages >= params.key){
                        callback.onResult(it.movieList, params.key+1)
                        networkState.postValue(NetworkState.LOADED)
                    }else{
                        networkState.postValue(NetworkState.END_OF_LIST)
                    }
                },{
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource", it.message ?: "Unknown error")
                })
        )
    }

    //load the previous page, will be called when user scrolls up
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        //doesn't need to do anything here, because RecyclerView is holding all previous data
    }
}