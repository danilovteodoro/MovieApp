package dev.danilovteodoro.movieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.databinding.MoviesLayoutBinding
import dev.danilovteodoro.movieapp.ui.activity.MovieActivity
import dev.danilovteodoro.movieapp.ui.adapter.MovieAdapter
import dev.danilovteodoro.movieapp.ui.viewmodel.MovieStateListener
import dev.danilovteodoro.movieapp.ui.viewmodel.MovieViewModel
import util.DataState
import util.onItemTouch

@AndroidEntryPoint
class TheatresFragment : Fragment() {

    private val viewModel: MovieViewModel by  viewModels()
    private var _binding: MoviesLayoutBinding? = null
    private val binding: MoviesLayoutBinding get() = _binding!!
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MoviesLayoutBinding.inflate(inflater)
        initRcMovies()
        binding.layoutError.btnRetry.setOnClickListener {
            hideError()
            viewModel.setStateListener(MovieStateListener.GetIntheatres)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObserver()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.moviesLv.value.let { dataState ->
            if(dataState == null){
                viewModel.setStateListener(MovieStateListener.GetIntheatres)
            }
        }
    }

    private fun initRcMovies(){
        val columns = resources.getInteger(R.integer.movieSpanCount)
        layoutManager = GridLayoutManager(requireContext(),columns)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) columns else 1
            }
        }
        adapter = MovieAdapter(requireContext(),getString(R.string.header_in_theatres))
        binding.rcMovies.layoutManager = layoutManager
        binding.rcMovies.adapter = adapter
        binding.rcMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!binding.rcMovies.canScrollVertically(1)){
                    viewModel.setStateListener(MovieStateListener.GetIntheatres)
                }
            }
        })

        binding.rcMovies.onItemTouch { position, _ ->
            adapter.get(position)?.let { movie->
                MovieActivity.start(requireActivity(),movie.id)
            }
        }
    }
    private fun registerObserver(){
        viewModel.moviesLv.observe(viewLifecycleOwner, Observer { dataState->
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