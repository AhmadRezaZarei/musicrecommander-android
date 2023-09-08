package code.name.monkey.retromusic.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongLogDao {

    @Insert
    suspend fun addSongLog(songLog: SongLogEntity): Long

    @Query("SELECT * FROM SongLogEntity ORDER BY timestamp DESC LIMIT :limit")
    fun getSongLogEntities(limit: Int): List<SongLogEntity>
    
    @Query("delete from SongLogEntity where id = :idList")
    fun deleteSongLogs(idList: List<Int>)



}