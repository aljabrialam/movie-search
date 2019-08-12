package appetisser.codingchallenge.itunes.injection

import android.media.MediaPlayer
import dagger.Module
import dagger.Provides

@Module
object MediaPlayerModule {

    @Provides
    @JvmStatic
    fun provideMediaPlayer() : MediaPlayer {
        return MediaPlayer()
    }

}