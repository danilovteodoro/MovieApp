package dev.danilovteodoro.movieapp.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.danilovteodoro.movieapp.di.NetworkModule
import dev.danilovteodoro.movieapp.network.api.GenreApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class TestGenreApi {
    private lateinit var genreApi:GenreApi

    @Before
    fun setup(){
        val gson = NetworkModule.providesGson()
        val retrofit = NetworkModule.providesRetrofit(gson)
        genreApi = NetworkModule.provideGenreApi(retrofit)
    }

    @Test
    fun testGetGenres() = runBlocking{
        val genreResult = genreApi.getGenres()
        assertNotNull(genreResult)
    }
}