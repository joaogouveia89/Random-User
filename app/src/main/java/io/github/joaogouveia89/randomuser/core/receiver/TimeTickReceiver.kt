package io.github.joaogouveia89.randomuser.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimeTickReceiver(private val onMinuteChanged: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_TIME_TICK) {
            onMinuteChanged()
        }
    }
}