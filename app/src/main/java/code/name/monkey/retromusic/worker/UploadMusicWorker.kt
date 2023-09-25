package code.name.monkey.retromusic.worker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import code.name.monkey.retromusic.db.MIGRATION_23_24
import code.name.monkey.retromusic.db.RetroDatabase
import code.name.monkey.retromusic.db.SongUploadHistoryEntity
import code.name.monkey.retromusic.network.RecommanderService
import code.name.monkey.retromusic.network.RequestSongLog
import code.name.monkey.retromusic.network.SongLogRequest
import code.name.monkey.retromusic.util.AudioUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


private const val TAG = "UploadMusicWorker"
class UploadMusicWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        if (!isNetworkAvailable(applicationContext)) {
            Log.e(TAG, "doWork: network is not available")
            return Result.success()
        }
        Log.e(TAG, "doWork: network is available" )

        val database = Room.databaseBuilder(applicationContext, RetroDatabase::class.java, "playlist.db")
            .addMigrations(MIGRATION_23_24)
            .build()

        val songLogDao = database.songLogDao()
        val uploadSongHistoryDao = database.songUploadHistoryDao()


        val logEntities = songLogDao.getSongLogEntities(50)

        var requestSongLogs = logEntities.map { entity ->
            val s = entity.song
            return@map RequestSongLog(
                id = s.id,
                title = s.title,
                year = s.year,
                duration = s.duration,
                data = s.data,
                dateModified = s.dateModified,
                albumId = s.albumId,
                albumName = s.albumName,
                artistId = s.artistId,
                artistName = s.artistName,
                composer = s.composer,
                albumArtist = s.albumArtist,
                songStartedAt = entity.songStartedAt,
                songEndedAt = entity.songEndAt,
                timestamp = entity.timestamp
            )
        }
        requestSongLogs = requestSongLogs.filter {
            it.data != ""
        }

        if (requestSongLogs.isEmpty()) {
            return Result.success()
        }

        // upload logs
        val service = RecommanderService.invoke()
        val res = service.sendLogs(SongLogRequest(logs = requestSongLogs))
        Log.e(TAG, "doWork: upload logs: " + res.code())
        if (res.code() != 200) {
            return Result.failure()
        }

        for (songLog in requestSongLogs) {

            val songId = songLog.id
            var historyEntity = uploadSongHistoryDao.getSongUploadHistory(songId)
            val isUploaded = historyEntity?.isUploaded ?: false

            if (isUploaded) {
                continue
            }

            val path = applicationContext.getExternalFilesDir(null)?.absolutePath
            if (path == null) {
                Log.e(TAG, "doWork: path is null")
                continue
            }

            Log.e(TAG, "doWork: path is " + path)

            val filepath = path + "/" + songId

            Log.e(TAG, "song path: " + songLog.data)
            AudioUtils.trim(songLog.data, filepath, 10, 40)
            val file = File(filepath)
            val fileBody: RequestBody = file.asRequestBody("audio/mpeg3".toMediaType())
            val songFile: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file.name, fileBody)

            val res = service.uploadSongLog(
                    songFile = songFile,
                    songId = songLog.id.toString().toRequestBody("text/plain".toMediaType()),
                    songTitle = songLog.title.toRequestBody("text/plain".toMediaType()),
                    year = songLog.year.toString().toRequestBody("text/plain".toMediaType()),
                    duration = songLog.duration.toString().toRequestBody("text/plain".toMediaType()),
                    date = songLog.data.toRequestBody("text/plain".toMediaType()),
                    albumId = songLog.albumId.toString().toRequestBody("text/plain".toMediaType()),
                    albumName = songLog.albumName.toRequestBody("text/plain".toMediaType()),
                    composer = songLog.composer?.toRequestBody("text/plain".toMediaType()),
                    artistName = songLog.artistName.toRequestBody("text/plain".toMediaType()),
                    albumArtist = songLog.albumArtist?.toRequestBody("text/plain".toMediaType()),
                    songStartedAt = songLog.songStartedAt.toString().toRequestBody("text/plain".toMediaType()),
                    songEndedAt = songLog.songEndedAt.toString().toRequestBody("text/plain".toMediaType()),
                    timestamp = songLog.timestamp.toString().toRequestBody("text/plain".toMediaType()),
                    artistId = songLog.artistId.toString().toRequestBody()
                )

            Log.e(TAG, "doWork: upload song 30s: " + res.code())

            if (res.code() == 200) {

                if(historyEntity == null) {
                    historyEntity = SongUploadHistoryEntity(id= 0, songId= songLog.id, isUploaded= true)
                }
                uploadSongHistoryDao.upsertSongUploadHistory(historyEntity)
                Log.e(TAG, "userted: " + songLog.title)
                continue
            }

        }

        // upload music 30s


        return Result.success()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw  = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

}