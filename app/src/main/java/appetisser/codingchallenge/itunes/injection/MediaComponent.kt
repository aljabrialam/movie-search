package appetisser.codingchallenge.itunes.injection

import dagger.Component
import appetisser.codingchallenge.itunes.ui.player.PlayerActivity

@Component(modules = [MediaPlayerModule::class])
interface MediaComponent {

    fun inject(playerActivity: PlayerActivity)

}