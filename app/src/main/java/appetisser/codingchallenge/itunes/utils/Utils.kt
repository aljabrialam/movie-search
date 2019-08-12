package appetisser.codingchallenge.itunes.utils

import appetisser.codingchallenge.itunes.R
import appetisser.codingchallenge.itunes.data.model.response.MovieResult
import java.util.*
import java.util.concurrent.TimeUnit


fun formatMillis(millis: Long) : String{
    return String.format("%2d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millis),
        TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
    );
}

fun Array<MovieResult>?.sortByTrackOrder(order: Int) = this?.let { res ->
    when(order){
        R.id.sort_price -> res.sortBy { it.trackPrice }
        R.id.sort_length -> res.sortBy { it.trackTimeMillis }
        R.id.sort_genre -> res.sortBy { it.primaryGenreName }
    }
}

fun Date.yearString() : String {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.YEAR).toString()
}