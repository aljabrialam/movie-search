package appetisser.codingchallenge.itunes.ui.moviesearch

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import appetisser.codingchallenge.itunes.R
import appetisser.codingchallenge.itunes.data.model.response.MovieResult
import appetisser.codingchallenge.itunes.utils.ImageLoader
import appetisser.codingchallenge.itunes.utils.formatMillis

class ResultsAdapter(private val context : Context, val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items : Array<MovieResult> = emptyArray()

    fun submitItems(arr: Array<MovieResult>){
        items = arr
        notifyDataSetChanged()
    }

    fun clearItems(){
        items = emptyArray()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieResultViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieResult = items[position]
        val movieViewHolder = holder as MovieResultViewHolder
        movieViewHolder.trackText.text = movieResult.trackName
        movieViewHolder.artistText.text = movieResult.artistName
        movieViewHolder.genre.text = movieResult.primaryGenreName
        movieViewHolder.genre.requestLayout()
        movieViewHolder.priceText.text = "${movieResult.trackPrice} ${movieResult.currency}"
        movieViewHolder.trackLengthText.text = formatMillis(movieResult.trackTimeMillis?.toLong() ?: 0)
        ImageLoader.loadImage(movieResult.artworkUrl100, movieViewHolder.artImage)
    }

    override fun getItemCount(): Int = items.size

    inner class MovieResultViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val trackText = view.text_title
        val artistText = view.text_artist
        val artImage = view.image_art
        val priceText = view.text_price
        val trackLengthText = view.text_trackLength
        val genre = view.text_genre

        init{
            view.setOnClickListener{
                onItemClick.invoke(adapterPosition)
            }
        }
    }

}