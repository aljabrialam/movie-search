# MVVM + LiveData + Room

## Summary
* Use [MVVM][1] using [architecture components][6] with to separate Android Framework with a [clean architecture][2] to domain logic.
* Use [Android Databinding][3] wih [LiveData][8] to glue [ViewModel][9] and Android
* Asynchronous communications implemented with AsyncTask[4].
* Rest API from [https://itunes.apple.com][5]
* Store data using [Room][7]

## Features
* search for a movie list with image, title, genre, artist, price and duration
* sorting of list by genre, movie length and price
* move to next or previous from the fetch list and play given audio fetch from itunes api
* share the movie via social media or send to email
* can add and remove with confirmation tags when user visited movie details to demonstrate persistence 


Languages, libraries and tools used

- [Kotlin](https://kotlinlang.org/)
- Dependency Injection with [Dagger](http://google.github.io/dagger/) A fast dependency injector for Kotlin and Java
- Persistence Library [Room](https://developer.android.com/topic/libraries/architecture/room.html) persistence library provides an abstraction layer over SQLite
- Network communication with [Retrofit](http://square.github.io/retrofit/) A type-safe HTTP client for Kotlin and Java
- Image loading and caching with [Picasso](https://square.github.io/picasso/) A powerful image downloading and caching library for Android
- [Moshi](https://github.com/square/moshi) is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects
- [AndroidTagView](https://github.com/whilu/AndroidTagView) An Android TagView library. You can customize awesome TagView by using this library.


## Dependencies
* architecture components
  * livedata - is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services.
  * room - persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
  * viewmodel - class is designed to store and manage UI-related data in a lifecycle conscious way

