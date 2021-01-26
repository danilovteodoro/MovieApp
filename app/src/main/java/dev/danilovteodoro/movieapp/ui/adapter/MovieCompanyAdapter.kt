package dev.danilovteodoro.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.danilovteodoro.movieapp.R
import dev.danilovteodoro.movieapp.model.Company

class MovieCompanyAdapter(context:Context):RecyclerView.Adapter<MovieCompanyAdapter.ViewHolder>()
{
    private val inflater = LayoutInflater.from(context)
    private var  companies:List<Company> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.inflater_movie_compay,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = companies[position]
        holder.txtCompany.text = current.name
        current.logoPath?.let {
            Picasso.get()
                    .load(current.getLogoPathUrl())
                    .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return companies.count()
    }

    fun add(companies:List<Company>){
        this.companies = companies
        notifyDataSetChanged()
    }

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val txtCompany:TextView = view.findViewById(R.id.txtMovieCompany)
        val imageView:ImageView = view.findViewById(R.id.imgMovieCompany)
    }
}