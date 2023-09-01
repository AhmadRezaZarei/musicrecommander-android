package code.name.monkey.retromusic.model

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
open class SongLog(
    var song: Song?,
    var songStartedAt: Int,
    var songEndAt: Int,
    var timestamp: Long
) : Parcelable
