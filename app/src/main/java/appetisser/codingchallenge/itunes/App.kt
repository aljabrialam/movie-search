package appetisser.codingchallenge.itunes

import android.app.Application
import appetisser.codingchallenge.itunes.injection.AppComponent
import appetisser.codingchallenge.itunes.injection.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}