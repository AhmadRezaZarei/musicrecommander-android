package code.name.monkey.retromusic.db

import androidx.room.*

@Dao
interface SongUploadHistoryDao {

    @Upsert()
    fun upsertSongUploadHistory(entity: SongUploadHistoryEntity)

    @Query("SELECT * FROM SongUploadHistoryEntity WHERE song_id = :songId")
    fun getSongUploadHistory(songId: Long): SongUploadHistoryEntity?

}

@Entity
data class SongUploadHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Long,
    @ColumnInfo("song_id")
    var songId: Long,
    @ColumnInfo("is_uploaded")
    var isUploaded: Boolean,
)