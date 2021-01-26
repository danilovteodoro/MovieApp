package dev.danilovteodoro.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.databinding.InflaterMovieBinding
import dev.danilovteodoro.movieapp.model.Movie

class MovieAdapter(context: Context,val headerText:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var movies:MutableList<Movie> = mutableListOf()
    private val contentType = 1
    private val headerType = 2

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)  headerType else contentType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType ==  contentType){
            val view = inflater.inflate(R.layout.inflater_movie,parent,false)
            ViewHolder(view)
        }else{
            val view = inflater.inflate(R.layout.header_layout,parent,false)
            HeaderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val current = movies[position-1]
            holder.txtMovie.text = current.title
            holder.txtRate.text = current.voteAverage.toString()
            holder.txtRate.setBackgroundResource(getResourceFromRate(current.voteAverage))
            current.posterPath?.let {
                Picasso.get().load(current.getPosterPathUrl())
                        .placeholder(R.drawable.movie_icon)
                        .error(R.drawable.movie_icon)
                        .into(holder.imgMovie)
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.count() + 1
    }

    fun add(movies:List<Movie>){
        val index = this.movies.size +1
        this.movies.addAll(movies)
        notifyItemRangeInserted(index,movies.count())
    }

    fun get(position: Int):Movie?{
        if(position == 0) return null
        return movies[position-1]
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val binding = InflaterMovieBinding.bind(view)
        val txtMovie = binding.txtMovie
        val imgMovie = binding.imgMovie
        val txtRate = binding.txtRate
    }
    inner class HeaderViewHolder(view:View):RecyclerView.ViewHolder(view){
        init {
            view.findViewById<TextView>(R.id.txtHeader)
                    .text = headerText
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
}