package com.samfonsec.lotr.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.samfonsec.lotr.data.api.CharacterApi
import com.samfonsec.lotr.data.api.MovieApi
import com.samfonsec.lotr.data.dataSource.CharacterDataSource
import com.samfonsec.lotr.data.dataSource.MovieDataSource
import com.samfonsec.lotr.data.database.FavoriteDatabase
import com.samfonsec.lotr.data.repository.CharacterRepository
import com.samfonsec.lotr.data.repository.MovieRepository
import com.samfonsec.lotr.util.Constants.Api.API_BASE_URL
import com.samfonsec.lotr.util.Constants.Api.API_KEY
import com.samfonsec.lotr.util.Constants.Api.HEADER_AUTH
import com.samfonsec.lotr.util.Constants.PREFERENCES_NAME
import com.samfonsec.lotr.viewModel.auth.AuthViewModel
import com.samfonsec.lotr.viewModel.characters.CharactersViewModel
import com.samfonsec.lotr.viewModel.characters.QuotesViewModel
import com.samfonsec.lotr.viewModel.main.favorites.FavoritesViewModel
import com.samfonsec.lotr.viewModel.main.home.HomeViewModel
import com.samfonsec.lotr.viewModel.main.security.SecurityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val authModule = module {

    fun provideSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            PREFERENCES_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    single { provideSharedPreferences(androidContext()) }
}

val moviesModule = module {
    single<MovieApi> { getRetrofit().create(MovieApi::class.java) }
    single { FavoriteDatabase.getInstance(androidContext()).favoriteDao() }
    single<MovieRepository> { MovieDataSource(get(), get()) }
}

val charactersModule = module {
    single<CharacterApi> { getRetrofit().create(CharacterApi::class.java) }
    single<CharacterRepository> { CharacterDataSource(get()) }
}

val viewModels = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CharactersViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { SecurityViewModel() }
    viewModel { AuthViewModel() }
    viewModel { QuotesViewModel(get()) }
}

val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        return okHttpClientBuilder
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor {
                val newRequest = it.request().newBuilder().addHeader(HEADER_AUTH, API_KEY).build()
                it.proceed(newRequest)
            }
            .build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get(), get()) }
}

private fun Scope.getRetrofit() = get<Retrofit>()