package appetisser.codingchallenge.itunes.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import appetisser.codingchallenge.itunes.App
import appetisser.codingchallenge.itunes.data.repository.MovieRepository
import appetisser.codingchallenge.itunes.data.model.response.MovieResult
import javax.inject.Inject

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var movieRepository : MovieRepository

    val movie : MediatorLiveData<MovieResult> = MediatorLiveData()
    var trackIndex : Int = 0
    var searchQuery : String = "star"

    init{
        getApplication<App>().appComponent.inject(this)
        movie.addSource(movieRepository.searchResults){
            movie.value = it[trackIndex]
        }
    }

    // MediaPlayer UI state
    enum class MediaPlayerState{
        NOT_READY, LOADING, PLAYING, PAUSED
    }

    val playerState : MutableLiveData<MediaPlayerState> = MutableLiveData(
        MediaPlayerState.NOT_READY
    )

    fun setPlayerState(state: MediaPlayerState){
        playerState.postValue(state)
    }

    // Track repository
    fun fetchTrack(index: Int){
        trackIndex = index
        if (movieRepository.hasResults()) {
            movie.postValue(movieRepository.getTrack(index))
        } else {
            movieRepository.search(searchQuery)
        }
    }

    fun nextTrack() : Int {
        if(trackIndex < movieRepository.searchResultCount - 1){
            fetchTrack(trackIndex + 1)
            return trackIndex + 1
        }
        return trackIndex
    }

    fun prevTrack() : Int {
        if(trackIndex > 0) {
            fetchTrack(trackIndex - 1)
            return trackIndex - 1
        }
        return trackIndex
    }

    fun results() : LiveData<Array<MovieResult>> = movieRepository.searchResults

}
