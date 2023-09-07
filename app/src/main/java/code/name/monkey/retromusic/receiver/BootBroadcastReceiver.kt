package code.name.monkey.retromusic.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import code.name.monkey.retromusic.util.ScheduleUtil

class BootBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(ctx: Context?, p1: Intent?) {
        Log.e("BootBroadcastReceiver", "onReceive called" )
        ctx?.let {
            ScheduleUtil.scheduleJob(it)
        }
    }
}