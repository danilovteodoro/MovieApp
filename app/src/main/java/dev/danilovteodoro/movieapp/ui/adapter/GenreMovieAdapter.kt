package dev.danilovteodoro.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.model.Genre

class GenreMovieAdapter(context:Context) : RecyclerView.Adapter<GenreMovieAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var genres:List<Genre> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.infalter_text,parent,false) as TextView
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = genres[position].name
    }

    override fun getItemCount(): Int {
        return genres.count()
    }
    fun add(genres:List<Genre>){
        this.genres = genres
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val textView: TextView):RecyclerView.ViewHolder(textView)
}