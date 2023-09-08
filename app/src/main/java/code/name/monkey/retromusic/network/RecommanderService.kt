package code.name.monkey.retromusic.network

import android.content.Context
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.*
import kotlin.collections.ArrayList


private const val PING = "/ping"
private const val SONG_LOG = "/song/log"
private const val BASE_URL = "http://192.168.94.224:3500"
private const val SONG_LOGS = "/song/logs"

interface RecommanderService {

    @Multipart
    @POST(SONG_LOG)
    fun uploadSongLog(
        @Part("file") songFile: RequestBody?,
        @Part("id") songId: RequestBody?,
        @Part("title") songTitle: RequestBody?,
        @Part("year") year: RequestBody?,
        @Part("duration") duration: RequestBody?,
        @Part("date") date: RequestBody?,
        @Part("album_id") albumId: RequestBody?,
        @Part("album_name") albumName: RequestBody?,
        @Part("composer") composer: RequestBody?,
        @Part("artist_name") artistName: RequestBody?,
        @Part("album_artist") albumArtist: RequestBody?,
        @Part("artist_id") artistId: RequestBody?,
        @Part("song_started_at") songStartedAt: RequestBody?,
        @Part("song_ended_at") songEndedAt: RequestBody?,
        @Part("timestamp") timestamp: RequestBody
    ): Response<BaseResponse>

    @POST(SONG_LOGS)
    suspend fun sendLogs(@Body req: SongLogRequest): Response<BaseResponse>

    suspend fun ping(
    ): Response<PingResponse>

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

data class BaseResponse(private var error: String?)

data class  SongLogRequest(
    @SerializedName("logs")
    private val logs: kotlin.collections.List<RequestSongLog>
)

data class RequestSongLog(
    var id: Long,
    val title: String,
    val year: Int,
    @SerializedName("duration")
    val duration: Long,
    val data: String,
    @SerializedName("date_modified")
    val dateModified: Long,
    @SerializedName("album_id")
    val albumId: Long,
    @SerializedName("album_name")
    val albumName: String,
    @SerializedName("artist_id")
    val artistId: Long,
    @SerializedName("artist_name")
    val artistName: String,
    val composer: String?,
    @SerializedName("album_artist")
    val albumArtist: String?,
    @SerializedName("song_started_at")
    val songStartedAt: Int,
    @SerializedName("song_ended_at")
    val songEndedAt: Int,
    @SerializedName("timestamp")
    val timestamp: Long

)