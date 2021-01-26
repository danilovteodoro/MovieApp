package dev.danilovteodoro.movieapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.databinding.ActivityMovieBinding
import dev.danilovteodoro.movieapp.model.Movie
import dev.danilovteodoro.movieapp.ui.adapter.GenreMovieAdapter
import dev.danilovteodoro.movieapp.ui.adapter.MovieCompanyAdapter
import dev.danilovteodoro.movieapp.ui.viewmodel.MovieStateListener
import dev.danilovteodoro.movieapp.ui.viewmodel.MovieViewModel
import util.DataState

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {
    private val viewModel:MovieViewModel by viewModels()
    private var _binding:ActivityMovieBinding? = null
    private val binding:ActivityMovieBinding get() = _binding!!
    private lateinit var genreAdapter:GenreMovieAdapter
    private lateinit var companiesAdapter:MovieCompanyAdapter

    companion object {
        const val MOVIE_ID_IT = "movieIdIt"
        fun start(activity:Activity,movieId:Long){
            val intent = Intent(activity,MovieActivity::class.java)
            intent.putExtra(MOVIE_ID_IT,movieId)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        setupLists()
        binding.layoutError.btnRetry.setOnClickListener {
            hideError()
            viewModel.setStateListener(MovieStateListener.GetMovieById)
        }
        registerObserver()
        if(viewModel.selectedMovieLv.value == null){
            viewModel.setStateListener(MovieStateListener.GetMovieById)
        }

    }

    fun setupActionBar(){
        setSupportActionBar(binding.appbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        binding.collapse.setExpandedTitleColor(Color.TRANSPARENT)
    }

    fun setupLists(){
        genreAdapter = GenreMovieAdapter(this)
        binding.rcGenres.layoutManager = GridLayoutManager(this,2)
        binding.rcGenres.adapter = genreAdapter

        companiesAdapter = MovieCompanyAdapter(this)
        binding.rcCompanies.layoutManager = GridLayoutManager(this,2)
        binding.rcCompanies.adapter = companiesAdapter
    }

    private fun registerObserver(){
        viewModel.selectedMovieLv.observe(this, Observer { dataState->
            when(dataState){
                is DataState.Loading ->{
                    showProgress()
                }

                is DataState.Success -> {
                    hideProgress()
                    updateLayout(dataState.value)
                }

                is DataState.ServerError -> {
                    hideProgress()
                    showError()
                    binding.layoutError.txtError.text = getString(R.string.serverError)
                }

                is DataState.Error -> {
                    hideProgress()
                    showError()
                    binding.layoutError.txtError.text = getString(R.string.genericError)
                }

                is DataState.NoConnection -> {
                    hideProgress()
                    showError()
                    binding.layoutError.txtError.text = getString(R.string.noConnectionError)
                }else ->{}
            }
        })
    }

    private fun updateLayout(movie:Movie){
        movie.backdropPath?.let {
            Picasso.get().load(movie.getBackdropPathUrl())
                    .placeholder(R.drawable.movie_icon)
                    .error(R.drawable.movie_icon)
                    .into(binding.imgCollapse)
        }
        movie.posterPath?.let {
            Picasso.get().load(movie.getPosterPathUrl())
                    .placeholder(R.drawable.movie_icon)
                    .error(R.drawable.movie_icon)
                    .into(binding.imgMovie)
        }

        binding.txtMovieName.text = movie.title
        binding.collapse.title = movie.title
        setTitle(movie.title)

        //update values
        binding.txtRate.text = movie.voteAverage.toString()
        binding.txtPopularity.text = movie.popularity.toString()
        binding.txtTotalVotes.text = movie.voteCount.toString()
        binding.txtRate.setBackgroundResource(getResourceFromRate(movie.voteAverage))

        //Update Overview
        binding.txtOverView.text = movie.overview

        //Update Lists
        movie.genres?.let { genres->
            genreAdapter.add(genres)
        }
        movie.productionCompanies?.let{ companies->
            companiesAdapter.add(companies)
        }

    }

    private fun getResourceFromRate(rate:Double):Int{
        return when {
            rate >6 -> {
                R.drawable.shape_rate_green
            }
            rate < 5 -> {
                R.drawable.shape_rate_red
            }
            else -> {
                R.drawable.shape_rate_yellow
            }
        }
    }

    private fun showProgress(){
        binding.progrress.visibility = View.VISIBLE
        binding.nestScroll.visibility = View.GONE
    }
    private fun hideProgress(){
        binding.progrress.visibility = View.GONE
        binding.nestScroll.visibility = View.VISIBLE
    }

    private fun showError(){
        binding.layoutError.mainLayout.visibility = View.VISIBLE
        binding.nestScroll.visibility = View.GONE
    }

    private fun hideError(){
        binding.layoutError.mainLayout.visibility = View.GONE
        binding.nestScroll.visibility = View.VISIBLE
    }


}