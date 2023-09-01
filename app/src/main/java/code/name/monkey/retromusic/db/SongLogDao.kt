package code.name.monkey.retromusic.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongLogDao {

    @Insert
    suspend fun addSongLog(songLog: SongLogEntity): Long

    @Query("SELECT * FROM SongLogEntity")
    fun getAllSongLogEntities(): List<SongLogEntity>

}