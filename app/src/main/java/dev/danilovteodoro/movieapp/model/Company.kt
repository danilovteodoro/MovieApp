package dev.danilovteodoro.movieapp.model

data class Company(
    val id:Long,
    val name:String,
    val logoPath:String?
)  {
    fun getLogoPathUrl():String{
        if(logoPath == null) return ""
        return "https://image.tmdb.org/t/p/w200/"
                .plus(logoPath)
    }
}
