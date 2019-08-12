package appetisser.codingchallenge.itunes.data.model.response

import java.util.*

data class SearchResult(val resultCount: Int, val results: Array<MovieResult>)

data class MovieResult(val trackId : Int,
                       val artistName : String?,
                       val trackName: String?,
                       val collectionName : String?,
                       val artworkUrl100 : String?,
                       val previewUrl : String?,
                       val primaryGenreName: String?,
                       val releaseDate : Date?,
                       val trackTimeMillis : Int?,
                       val trackPrice : Float?,
                       val currency: String?,
                       val trackViewUrl: String?,
                       val longDescription: String?) {

    companion object {
        fun empty() : MovieResult {
            return MovieResult(
                0,
                "",
                ",",
                "",
                "",
                "",
                "",
                Date(),
                0,
                0f,
                "",
                "",
                ""
            )
        }
    }

}