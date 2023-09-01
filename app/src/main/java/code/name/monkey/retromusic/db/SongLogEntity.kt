package code.name.monkey.retromusic.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import code.name.monkey.retromusic.model.Song
import com.google.gson.annotations.SerializedName

@Entity
data class SongLogEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="row_id")
    val id: Long,
    @Embedded
    val song: Song,
    @ColumnInfo("song_started_at")
    var songStartedAt: Int,
    @ColumnInfo("song_ended_at")
    var songEndAt: Int,
    @ColumnInfo("timestamp")
    var timestamp: Long
)