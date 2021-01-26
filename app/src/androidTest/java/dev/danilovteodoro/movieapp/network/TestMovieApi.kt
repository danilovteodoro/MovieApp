package dev.danilovteodoro.movieapp.network


import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.danilovteodoro.movieapp.di.NetworkModule
import dev.danilovteodoro.movieapp.network.api.MovieApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import util.Constantes
import util.DateUtil
import java.util.*

@RunWith(AndroidJUnit4::class)
class TestMovieApi {
    private lateinit var movieApi: MovieApi

    @Before
    fun setup(){
        val gson = NetworkModule.providesGson()
        val retrofit = NetworkModule.providesRetrofit(gson)
        movieApi = NetworkModule.providesMovieApi(retrofit)
    }

    @Test
    fun testGetMoviesInTheatres() = runBlocking {
        val minDate:Date = DateUtil.plusDays(Date(),-5)
        val maxDate:Date = DateUtil.plusDays(Date(),5)
        val minDateString = DateUtil.format(minDate,Constantes.DATE_FORMAT)
        val maxDateString = DateUtil.format(maxDate,Constantes.DATE_FORMAT)
        val response = movieApi.getMoviesIntheatres(minDateString,maxDateString)
        assertNotNull(response)

    }

    @Test
    fun testGetMovieById() = runBlocking{
        val movie = movieApi.getMovieById(775996)
        assertNotNull(movie)
    }

    @Test
    fun testGetByPopolarity() = runBlocking{
        val movieResult = movieApi.getByPopularity()
        assertNotNull(movieResult)
    }

    @Test
    fun getByGenre() = runBlocking{
        val movieResults = movieApi.getByGenre(35)
        assertNotNull(movieResults)
    }
}