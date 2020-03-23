/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.cataloguemovie2.data.model.Movie
import com.example.cataloguemovie2.data.repository.NetworkState
import com.example.cataloguemovie2.view.ui.popularmovie.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class PopularMovieViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun isListEmpty(): Boolean = moviePagedList.value?.isEmpty() ?: true

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}