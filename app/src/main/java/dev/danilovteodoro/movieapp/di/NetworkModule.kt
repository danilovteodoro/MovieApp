package dev.danilovteodoro.movieapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.danilovteodoro.movieapp.network.api.GenreApi
import dev.danilovteodoro.movieapp.network.api.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.Constantes
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesGson():Gson{
        return GsonBuilder().
                setDateFormat(Constantes.DATE_FORMAT)
            .create()
    }

   @Provides
   @Singleton
   fun providesRetrofit(gson: Gson):Retrofit.Builder{
       return Retrofit.Builder()
           .baseUrl(Constantes.BASE_URL)
           .addConverterFactory(GsonConverterFactory.create(gson))
   }

    @Provides
    @Singleton
    fun providesMovieApi(retrofit: Retrofit.Builder):MovieApi{
        return retrofit.build().create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGenreApi(retrofit: Retrofit.Builder):GenreApi{
        return retrofit.build().create(GenreApi::class.java)
    }

}