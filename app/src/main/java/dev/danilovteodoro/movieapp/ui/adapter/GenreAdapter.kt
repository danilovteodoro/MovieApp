package dev.danilovteodoro.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.model.Genre

class GenreAdapter(context: Context):RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var genres:List<Genre> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.inflater_genre,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = genres[position]
        holder.textGenre.text = current.name
    }

    override fun getItemCount(): Int {
        return genres.count()
    }

    fun add(genres:List<Genre>){
        this.genres = genres
        notifyDataSetChanged()
    }

    fun get(position: Int):Genre {
        return genres[position]
    }

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val textGenre:TextView = view.findViewById(R.id.txtGenre)
    }
}