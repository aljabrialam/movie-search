package appetisser.codingchallenge.itunes.injection

import dagger.Component
import appetisser.codingchallenge.itunes.ui.player.PlayerViewModel
import appetisser.codingchallenge.itunes.ui.moviesearch.SearchViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,NetworkModule::class])
interface AppComponent {

    fun inject(playerViewModel: PlayerViewModel)
    fun inject(searchViewModel: SearchViewModel)

}