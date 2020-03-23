/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.view.ui.popularmovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cataloguemovie2.R
import com.example.cataloguemovie2.data.api.POSTER_BASE_URL
import com.example.cataloguemovie2.data.model.Movie
import com.example.cataloguemovie2.data.repository.NetworkState
import com.example.cataloguemovie2.view.ui.singlemoviedetails.SingleMovieActivity
import kotlinx.android.synthetic.main.item_movie_list.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*

class PopularMoviePagedListAdapter(val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    
    private var networkState: NetworkState? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View

        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.item_movie_list, parent, false)
            return MovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE)
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        else
            (holder as NetworkStateItemViewHolder).bind(networkState)
    }

    private fun hasExtraRow(): Boolean = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + when {
            hasExtraRow() -> 1
            else -> 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasExtraRow() && position == itemCount - 1 -> NETWORK_VIEW_TYPE
            else -> MOVIE_VIEW_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow) notifyItemRemoved(super.getItemCount())
            else notifyItemInserted(super.getItemCount())
        }else if(hasExtraRow && previousState != newNetworkState) notifyItemChanged(itemCount - 1)
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(movie: Movie?, context: Context){
            itemView.tvMovieTitle.text = movie?.title
            itemView.tvMovieReleaseDate.text = movie?.releaseDate

            val moviePosterUrl = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(itemView.ivMoviePoster)

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovieActivity::class.java).apply {
                    putExtra("id", movie?.id)
                }.also {
                    context.startActivity(it)
                }
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING) itemView.progressCircularItem.visibility = View.VISIBLE
            else itemView.progressCircularItem.visibility = View.GONE

            if(networkState != null && networkState == NetworkState.ERROR){
                itemView.tvErrorMsgItem.visibility = View.VISIBLE
                itemView.tvErrorMsgItem.text = networkState.msg
            }else if(networkState != null && networkState == NetworkState.END_OF_LIST){
                itemView.tvErrorMsgItem.visibility = View.VISIBLE
                itemView.tvErrorMsgItem.text = networkState.msg
            }else itemView.tvErrorMsgItem.visibility = View.GONE
        }
    }
}