package code.name.monkey.retromusic.worker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import code.name.monkey.retromusic.db.MIGRATION_23_24
import code.name.monkey.retromusic.db.RetroDatabase
import code.name.monkey.retromusic.network.RecommanderService
import code.name.monkey.retromusic.network.RequestSongLog
import code.name.monkey.retromusic.network.SongLogRequest


private const val TAG = "UploadMusicWorker"
class UploadMusicWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        if (!isNetworkAvailable(applicationContext)) {
            Log.e(TAG, "doWork: network is not available")
            return Result.success()
        }
        Log.e(TAG, "doWork: network is available" )

        val songLogDao = Room.databaseBuilder(applicationContext, RetroDatabase::class.java, "playlist.db")
            .addMigrations(MIGRATION_23_24)
            .build().songLogDao()



        val logEntities = songLogDao.getSongLogEntities(50)

        val requestSongLogs = logEntities.map { entity ->
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

        // upload logs
        val service = RecommanderService.invoke()
        service.sendLogs(SongLogRequest(logs = requestSongLogs))


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