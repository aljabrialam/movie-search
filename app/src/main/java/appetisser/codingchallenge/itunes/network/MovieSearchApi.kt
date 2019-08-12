package appetisser.codingchallenge.itunes.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import appetisser.codingchallenge.itunes.data.model.response.SearchResult

interface MovieSearchApi {

    @GET("/search")
    fun getMovieResults(@Query("term") query : String,
                        @Query("country") country : String,
                        @Query("media") media : String) : Call<SearchResult>

}