package appetisser.codingchallenge.itunes.ui.moviesearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import appetisser.codingchallenge.itunes.App
import appetisser.codingchallenge.itunes.data.repository.MovieRepository
import appetisser.codingchallenge.itunes.data.model.response.MovieResult
import appetisser.codingchallenge.itunes.data.repository.MovieDbRepository
import appetisser.codingchallenge.itunes.db.Movie
import appetisser.codingchallenge.itunes.db.MovieDao
import appetisser.codingchallenge.itunes.db.MovieDatabase
import appetisser.codingchallenge.itunes.utils.sortByTrackOrder
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var movieRepository: MovieRepository
    private val searchResults = MediatorLiveData<Array<MovieResult>>()

    private var movidDbrepository: MovieDbRepository = MovieDbRepository(application)
    private var allMovies: LiveData<List<Movie>> = movidDbrepository.getMovies()

    var currentOrder = 0
    var searchQuery: String = ""

    init {
        getApplication<App>().appComponent.inject(this)
        searchResults.addSource(movieRepository.searchResults) { results: Array<MovieResult> ->
            results.sortByTrackOrder(currentOrder)
            searchResults.value = results
        }
    }

    fun setQuery(query: String) {
        currentOrder = 0
        searchQuery = query
        movieRepository.search(query)
    }

    fun results(): LiveData<Array<MovieResult>> = searchResults

    fun error(): LiveData<String> = movieRepository.searchError

    fun sortResultsBy(sortId: Int) {
        currentOrder = sortId
        val results = movieRepository.searchResults.value
        results.sortByTrackOrder(currentOrder)
        searchResults.postValue(results)
    }

    // data manipulation from db
    fun insert(movie: Movie) {
        movidDbrepository.insert(movie)
    }

    fun delete(movie: Movie) {
        movidDbrepository.delete(movie)
    }

    fun deleteByTitle(title: String) {
        movidDbrepository.deleteByTitle(title)
    }

    fun deleteAllMovies() {
        movidDbrepository.deleteAllMovies()
    }

    fun getAllMovies(): LiveData<List<Movie>> {
        return allMovies
    }

}