package appetisser.codingchallenge.itunes.data.repository


import androidx.lifecycle.MutableLiveData
import appetisser.codingchallenge.itunes.data.model.response.SearchResult
import appetisser.codingchallenge.itunes.data.model.response.MovieResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import appetisser.codingchallenge.itunes.network.MovieSearchApi
import appetisser.codingchallenge.itunes.utils.MOVIE_SEARCH_DEFAULT_COUNTRY
import appetisser.codingchallenge.itunes.utils.MOVIE_SEARCH_DEFAULT_MEDIA
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieSearchApi: MovieSearchApi) {

    val searchResults: MutableLiveData<Array<MovieResult>> = MutableLiveData()
    val searchError: MutableLiveData<String> = MutableLiveData()
    var searchResultCount : Int = 0
    var searchRequest : Call<SearchResult>? = null


    fun search(query: String) {

        searchRequest?.cancel()
        if(query.isEmpty()){
            searchError.postValue("No results")
            return
        }
        searchRequest = movieSearchApi.getMovieResults(query, MOVIE_SEARCH_DEFAULT_COUNTRY, MOVIE_SEARCH_DEFAULT_MEDIA)
        searchRequest!!.enqueue(object: Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                response.body()?.let{
                    searchResultCount = it.resultCount
                    searchResults.postValue(it.results)
                }
            }
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                if(!call.isCanceled) searchError.postValue(t.localizedMessage)
            }
        })
    }

    fun getTrack(index: Int): MovieResult {
        return searchResults.value?.get(index) ?: MovieResult.empty()
    }

    fun hasResults(): Boolean{
        return searchResultCount > 0
    }

}