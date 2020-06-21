package com.androidstation.trendingmovies.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.androidstation.trendingmovies.data.api.POST_PER_PAGE
import com.androidstation.trendingmovies.data.api.TheMovieDBInterface
import com.androidstation.trendingmovies.data.repository.MovieDataSource
import com.androidstation.trendingmovies.data.repository.MovieDataSourceFactory
import com.androidstation.trendingmovies.data.repository.NetworkState
import com.androidstation.trendingmovies.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository (private val apiService: TheMovieDBInterface) {

    lateinit var moviePagedList :LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveDataPagedList(compositeDisposable: CompositeDisposable):LiveData<PagedList<Movie>>{

        moviesDataSourceFactory = MovieDataSourceFactory(apiService ,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory ,config).build()

        return moviePagedList
    }

    fun getNetworkState (): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource , NetworkState>(

            moviesDataSourceFactory.movieLiveDataSource ,MovieDataSource::networkState
        )


    }
}