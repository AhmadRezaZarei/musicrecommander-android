package code.name.monkey.retromusic.util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import code.name.monkey.retromusic.service.recommander.UploadMusicJobService


object ScheduleUtil {
    // schedule the start of the service every 10 - 30 seconds
    fun scheduleJob(context: Context) {
        Log.e("ScheduleUtil", "scheduleJob: ")
        val serviceComponent = ComponentName(context, UploadMusicJobService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((10 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((30 * 1000).toLong()) // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
    }
}