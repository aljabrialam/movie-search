package appetisser.codingchallenge.itunes.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import appetisser.codingchallenge.itunes.utils.MOVIE_TABLE_NAME


//The entity is just a POJO which is also going to be the table in the database.
//For example, you can create a POJO class and annotate it with the “@Entity” annotation.
//You can also identify which field is the primary key with “@PrimaryKey”.

@Entity(tableName = MOVIE_TABLE_NAME)
data class Movie(
    var trackId : Int,
    var title: String,
    var description: String

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}