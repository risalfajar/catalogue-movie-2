/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.repository

enum class Status {
    RUNNING, SUCCESS, FAILED
}

class NetworkState(val status: Status, val msg: String) {
    companion object{
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val END_OF_LIST: NetworkState

        init{
            LOADING = NetworkState(Status.RUNNING, "Running")
            LOADED = NetworkState(Status.SUCCESS, "Success")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
            END_OF_LIST = NetworkState(Status.FAILED, "You have reached the end")
        }
    }
}