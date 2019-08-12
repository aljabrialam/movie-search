package appetisser.codingchallenge.itunes.db

import androidx.lifecycle.LiveData
import androidx.room.*


//The Data Access Object (DAO) is an interface annotated with Dao.
//This is where the database CRUD (create, read, update and delete) operations are defined.
//Each method is annotated with “@Insert”, “@Delete”, “@Query(SELECT * FROM)”.

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(movie: Movie)

    @Query("DELETE FROM movies_table")
    fun deleteMovies()

    @Query("SELECT * FROM movies_table ")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT COUNT(*) FROM movies_table WHERE trackId = :trackId")
    fun getMovie(trackId: Array<out Int?>): Int

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM movies_table WHERE title = :title")
    fun deleteByTitle(title: Array<out String?>)

}