package me.mathazak.myyelp.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.mathazak.myyelp.api.YelpApiService
import me.mathazak.myyelp.data.BusinessDao
import me.mathazak.myyelp.data.YelpDatabase
import me.mathazak.myyelp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideYelpApiService(): YelpApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.BASE_URL)
            .build()

        return retrofit.create(YelpApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): YelpDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            YelpDatabase::class.java,
            "business_database",
        )
            .build()

    @Provides
    fun provideDao(database: YelpDatabase): BusinessDao = database.businessDao()
}