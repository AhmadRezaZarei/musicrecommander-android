package code.name.monkey.retromusic.network

import android.content.Context
import code.name.monkey.retromusic.model.DeezerResponse
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.*

private const val PING = "/ping"
private const val BASE_URL = "http://172.30.4.212:3500"

interface RecommanderService {

    @GET(PING)
    fun ping(
    ): Call<PingResponse>

    companion object {
        operator fun invoke(
        ): RecommanderService {
            return Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
        }

        fun createDefaultOkHttpClient(
            context: Context
        ): OkHttpClient.Builder = OkHttpClient.Builder()

    }

}

data class PingResponse(private var message: String)