package dev.danilovteodoro.movieapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.databinding.MoviesLayoutBinding
import dev.danilovteodoro.movieapp.model.Genre
import dev.danilovteodoro.movieapp.ui.adapter.MovieAdapter
import dev.danilovteodoro.movieapp.ui.viewmodel.MovieStateListener
import dev.danilovteodoro.movieapp.ui.viewmodel.MovieViewModel
import util.DataState
import util.onItemTouch

@AndroidEntryPoint
class GenreActivity : AppCompatActivity() {
    private val viewModel:MovieViewModel by viewModels()
    private var _binding:MoviesLayoutBinding? = null
    private val binding:MoviesLayoutBinding get() = _binding!!
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: MovieAdapter
    private lateinit var genero:Genre

    companion object {
        const val IT_GENRE = "iteGenre"
        fun start(activity:Activity,genre: Genre){
            val intent = Intent(activity,GenreActivity::class.java)
            intent.putExtra(IT_GENRE,genre)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MoviesLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genero = intent.getSerializableExtra(IT_GENRE) as Genre
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initRcMovies()

        binding.layoutError.btnRetry.setOnClickListener {
            hideError()
            viewModel.setStateListener(MovieStateListener.GetPopulars)
        }

        registerObserver()

        viewModel.moviesLv.value.let { dataState ->
            if(dataState == null){
                viewModel.setStateListener(MovieStateListener.GetByGenres)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    

    private fun initRcMovies(){
        val columns = resources.getInteger(R.integer.movieSpanCount)
        layoutManager = GridLayoutManager(this,columns)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) columns else 1
            }
        }
        val header = "${genero.name} Movies"
        adapter = MovieAdapter(this, header)
        binding.rcMovies.layoutManager = layoutManager
        binding.rcMovies.adapter = adapter
        binding.rcMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!binding.rcMovies.canScrollVertically(1)){
                    viewModel.setStateListener(MovieStateListener.GetByGenres)
                }
            }
        })
        binding.rcMovies.onItemTouch { position, _ ->
            adapter.get(position)?.let { movie->
                MovieActivity.start(this,movie.id)
            }
        }
    }

    private fun registerObserver(){
        viewModel.moviesLv.observe(this, Observer { dataState->
            when(dataState){
                is DataState.Loading -> showProgress()
                is DataState.Success -> {
                    hideProgress()
                    adapter.add(dataState.value)
                    binding.rcMovies.scrollBy(binding.rcMovies.scrollX,binding.rcMovies.scrollY + 100)

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

    private fun showProgress(){
        if(adapter.itemCount == 1){
            binding.contentProgress.visibility = View.VISIBLE
            binding.rcMovies.visibility = View.GONE
            return
        }
        binding.progrress.visibility = View.VISIBLE
    }

    private fun hideProgress(){
        binding.contentProgress.visibility = View.GONE
        binding.progrress.visibility = View.GONE
        binding.rcMovies.visibility = View.VISIBLE
    }


    private fun showError(){
        binding.layoutError.mainLayout.visibility = View.VISIBLE
        binding.rcMovies.visibility = View.GONE
    }

    private fun hideError(){
        binding.layoutError.mainLayout.visibility = View.GONE
        binding.rcMovies.visibility = View.VISIBLE
    }
}