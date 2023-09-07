package code.name.monkey.retromusic.service.recommander

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import code.name.monkey.retromusic.util.ScheduleUtil


class UploadMusicJobService : JobService() {

    override fun onStartJob(param: JobParameters?): Boolean {

        ScheduleUtil.scheduleJob(applicationContext)
        Log.e("UploadMusicService", "onStartJob")
        return true
    }

    override fun onStopJob(param: JobParameters?): Boolean {
        Log.e("UploadMusicService", "onStopJob")
        return true
    }

}