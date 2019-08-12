package appetisser.codingchallenge.itunes.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_movie_detail.*
import appetisser.codingchallenge.itunes.R
import android.media.MediaPlayer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import appetisser.codingchallenge.itunes.injection.DaggerMediaComponent
import appetisser.codingchallenge.itunes.ui.player.PlayerViewModel.*
import appetisser.codingchallenge.itunes.ui.player.PlayerViewModel.MediaPlayerState.*
import appetisser.codingchallenge.itunes.ui.moviesearch.SearchActivity
import appetisser.codingchallenge.itunes.utils.ImageLoader
import appetisser.codingchallenge.itunes.utils.yearString
import javax.inject.Inject
import android.text.method.ScrollingMovementMethod
import appetisser.codingchallenge.itunes.db.Movie
import appetisser.codingchallenge.itunes.ui.moviesearch.SearchActivity.Companion.SEARCH_QUERY
import appetisser.codingchallenge.itunes.ui.moviesearch.SearchActivity.Companion.TRACK_INDEX
import appetisser.codingchallenge.itunes.ui.moviesearch.SearchViewModel


class PlayerActivity : AppCompatActivity() {

    @Inject lateinit var mediaPlayer: MediaPlayer
    private lateinit var playerViewModel : PlayerViewModel
    private lateinit var searchViewModel : SearchViewModel

    private var currentTrackPreviewUrl = ""

    private var mediaPlayerState : MediaPlayerState = NOT_READY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        supportActionBar?.elevation = 0f
        title = ""
        image_play.isEnabled = false
        image_prev.isEnabled = false
        image_next.isEnabled = false

        DaggerMediaComponent.create().inject(this)

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        loadState(intent.extras)

        text_descriptions.movementMethod = ScrollingMovementMethod()

        playerViewModel.movie.observe(this, Observer {
            text_album.text = getString(R.string.album_title, it.trackName, it.artistName)
            text_artist.text = it.artistName
            text_track.text = getString(R.string.album_title,it.collectionName,it.releaseDate?.yearString())
            text_descriptions.text = it.longDescription
            ImageLoader.loadImage(it.artworkUrl100, image_art)
            currentTrackPreviewUrl = it.previewUrl ?: ""
            image_play.isEnabled = true
            image_prev.isEnabled = true
            image_next.isEnabled = true
            progress.visibility = View.GONE

            val movie = Movie(it.trackId, it.trackName.orEmpty(), it.longDescription.orEmpty())
            searchViewModel.insert(movie)
        })

        playerViewModel.playerState.observe(this, Observer { mediaPlayerState ->
            this.mediaPlayerState = mediaPlayerState
            when(mediaPlayerState!!){
                LOADING -> {
                    image_play.isEnabled = false
                    streamProgress.visibility = View.VISIBLE
                }
                PLAYING -> {
                    image_play.isEnabled = true
                    streamProgress.visibility = View.INVISIBLE
                    image_play.setImageResource(R.drawable.ic_pause)

                }
                NOT_READY, PAUSED -> image_play.setImageResource(R.drawable.ic_play)
            }
        })

        image_play.setOnClickListener {
            when (mediaPlayerState) {
                NOT_READY -> setupPlayer(currentTrackPreviewUrl)
                PLAYING -> {
                    mediaPlayer.pause()
                    playerViewModel.setPlayerState(PAUSED)
                }
                PAUSED -> {
                    mediaPlayer.start()
                    playerViewModel.setPlayerState(PLAYING)
                }
                else -> println(mediaPlayer)
            }
        }

        image_next.setOnClickListener{
            playerViewModel.nextTrack()
            resetPlayer()
        }

        image_prev.setOnClickListener{
            playerViewModel.prevTrack()
            resetPlayer()
        }

    }

    private fun setupPlayer(dataSource: String){
        if(dataSource.isEmpty()){
            return
        }
        playerViewModel.setPlayerState(LOADING)
        mediaPlayer.isLooping = true
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
            playerViewModel.setPlayerState(PLAYING)
        }
    }

    private fun resetPlayer(){
        streamProgress.visibility = View.INVISIBLE
        mediaPlayer.stop()
        mediaPlayer.reset()
        playerViewModel.setPlayerState(NOT_READY)
    }

    private fun loadState(bundle: Bundle?) {
        playerViewModel.searchQuery = bundle?.getString(SEARCH_QUERY) ?: ""
        playerViewModel.trackIndex = bundle?.getInt(TRACK_INDEX) ?: -1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(TRACK_INDEX, playerViewModel.trackIndex)
        outState.putString(SEARCH_QUERY, playerViewModel.searchQuery)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        loadState(savedInstanceState)
        playerViewModel.fetchTrack(playerViewModel.trackIndex)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_player,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.share){
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share_track)
                .setText(playerViewModel.movie.value?.trackViewUrl)
                .startChooser()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }

}
