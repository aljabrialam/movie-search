package appetisser.codingchallenge.itunes.injection

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import appetisser.codingchallenge.itunes.network.MovieSearchApi
import appetisser.codingchallenge.itunes.utils.MOVIE_SEARCH_API_BASE_URL
import javax.inject.Singleton
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.util.*


@Module
object NetworkModule{

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideTrackSearchAPI(retrofit: Retrofit) : MovieSearchApi {
        return retrofit.create(MovieSearchApi::class.java)
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRetrofit(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(MOVIE_SEARCH_API_BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
    }

}