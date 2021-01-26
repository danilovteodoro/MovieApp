package dev.danilovteodoro.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.databinding.FragmentGenreBinding
import dev.danilovteodoro.movieapp.ui.activity.GenreActivity
import dev.danilovteodoro.movieapp.ui.adapter.GenreAdapter
import dev.danilovteodoro.movieapp.ui.viewmodel.GenreStateListener
import dev.danilovteodoro.movieapp.ui.viewmodel.GenreViewModel
import util.DataState
import util.onItemTouch

@AndroidEntryPoint
class GenreFragment : Fragment() {
    private val viewModel:GenreViewModel  by viewModels()
    private var _binding:FragmentGenreBinding? = null
    private val binding:FragmentGenreBinding get() = _binding!!
    private lateinit var adapter:GenreAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGenreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcGenres.layoutManager = LinearLayoutManager(context)
        adapter = GenreAdapter(requireContext())
        binding.rcGenres.adapter = adapter
        binding.rcGenres.onItemTouch { position, _ ->
            GenreActivity.start(requireActivity(),adapter.get(position))
        }
        binding.layoutError.btnRetry.setOnClickListener {
            hideError()
            viewModel.setStateListener(GenreStateListener.GetGenres)
        }
        registerObserver()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.genresLv.value == null){
            viewModel.setStateListener(GenreStateListener.GetGenres)
        }
    }

    private fun registerObserver(){
        viewModel.genresLv.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Loading -> showProgress()
                is DataState.Success -> {
                    hideProgress()
                    adapter.add(dataState.value)
  
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
        binding.rcGenres.visibility = View.GONE
        binding.progrress.visibility = View.VISIBLE
    }
    private fun  hideProgress(){
        binding.rcGenres.visibility = View.VISIBLE
        binding.progrress.visibility = View.GONE
    }


    private fun showError(){
        binding.layoutError.mainLayout.visibility = View.VISIBLE
        binding.rcGenres.visibility = View.GONE
    }

    private fun hideError(){
        binding.layoutError.mainLayout.visibility = View.GONE
        binding.rcGenres.visibility = View.VISIBLE
    }




}