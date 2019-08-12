package appetisser.codingchallenge.itunes.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import appetisser.codingchallenge.itunes.utils.MOVIE_DATABASE_NAME

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    private var instance: MovieDatabase? = null

    companion object {
        private var instance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase? {
            if (instance == null) {
                synchronized(MovieDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java, MOVIE_DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }

    }

    class PopulateDbAsyncTask(db: MovieDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val movieDao = db?.movieDao()

        override fun doInBackground(vararg p0: Unit?) {
            //try inserting movie title
            //movieDao?.insert(Movie(1,"Title 1", "description 1"))
            //movieDao?.insert(Movie(2,"Title 2", "description 2"))
            //movieDao?.insert(Movie(3,"Title 3", "description 3"))
        }
    }

}