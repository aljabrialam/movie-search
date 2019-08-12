package appetisser.codingchallenge.itunes.data.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import appetisser.codingchallenge.itunes.db.Movie
import appetisser.codingchallenge.itunes.db.MovieDao
import appetisser.codingchallenge.itunes.db.MovieDatabase

class MovieDbRepository(application: Application) {

    private var movieDao: MovieDao

    private var allMovies: LiveData<List<Movie>>

    init {
        val database: MovieDatabase = MovieDatabase.getInstance(
            application.applicationContext
        )!!
        movieDao = database.movieDao()
        allMovies = movieDao.getMovies()
    }

    fun insert(movie: Movie) {
        val movieExist = getMovieByTitleAsyncTask(movieDao).execute(movie.trackId).get()
        if (movieExist == 0) { // check if record exist
            insertMovieAsyncTask(movieDao).execute(movie)
        }
    }

    fun delete(movie: Movie) {
        val deleteMovieAsyncTask = deleteMovieAsyncTask(movieDao).execute(movie)
    }

    fun deleteByTitle(title: String) {
        val deleteByTitleMovieAsyncTask = deleteMovieByTitleAsyncTask(movieDao).execute(title)
    }

    fun deleteAllMovies() {
        val deleteAllMoviesAsyncTask = DeleteAllMoviesAsyncTask(
            movieDao
        ).execute()
    }

    fun getMovies(): LiveData<List<Movie>> {
        return allMovies
    }

    private class getMovieByTitleAsyncTask(val movieDao: MovieDao) :  AsyncTask<Int, Unit, Int>() {
        override fun doInBackground(vararg trackId: Int?): Int {
            return movieDao.getMovie(trackId)
        }
    }

    private class insertMovieAsyncTask(val movieDao: MovieDao) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg movie: Movie?) {
            movieDao.insert(movie[0]!!)
        }
    }

    private class deleteMovieByTitleAsyncTask(val movieDao: MovieDao) :  AsyncTask<String, Unit, Unit>() {
        override fun doInBackground(vararg title: String?) {
            movieDao.deleteByTitle(title)
        }
    }

    private class deleteMovieAsyncTask(val movieDao: MovieDao) :  AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg movie: Movie?) {
            movieDao.delete(movie[0]!!)
        }
    }

    private class DeleteAllMoviesAsyncTask(val noteDao: MovieDao) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg p0: Unit?) {
            noteDao.deleteMovies()
        }
    }

}