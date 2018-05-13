package com.meishu.android.itfinder.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.meishu.android.itfinder.R
import com.meishu.android.itfinder.data.DataCenter
import com.meishu.android.itfinder.data.QueryPreferences
import com.meishu.android.itfinder.model.Post
import com.meishu.android.itfinder.provider.TimePadProvider
import java.util.concurrent.TimeUnit

class TrackService(private val tag: String = "TrackService") : IntentService(tag) {

    companion object {
        private val POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1)
        const val TRACKED_CHANNEL_ID = "com.meishu.android.itfinder.channel.tracked"
        const val TRACKED_CHANNEL_NAME = "ITFinder Tracked Channel"

        private fun newIntent(context: Context) = Intent(context, TrackService::class.java)

        fun setServiceAlarm(context: Context, isOn: Boolean) {
            val intent = newIntent(context)
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (isOn) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pendingIntent)
            } else {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }

        fun isServiceAlarmOn(context: Context): Boolean {
            val i = newIntent(context)
            val pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE)
            return pi != null
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (!isNetworkAvailableAndConnected()) {
            return
        }

        Log.i(tag, "Received intent: $intent")
        val query = QueryPreferences.getStoredQuery(this, QueryPreferences.PREF_TRACKED_QUERY)
        val lastResult = QueryPreferences.getStoredQuery(this, QueryPreferences.PREF_LAST_RESULT_IT)

        val results = DataCenter.provideData(query)
        var resultId : String? = null

        if (!results.isEmpty()) {
            resultId = results[0].title
            if (resultId == lastResult) {
                Log.i(tag, "Old result: $resultId")
            } else {
                Log.i(tag, "New result: $resultId")
                query?.let { createNotification(it, resultId) }
            }
        }

        QueryPreferences.setStoredQuery(this, QueryPreferences.PREF_LAST_RESULT_IT, resultId)
    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected ?: false
    }

    private fun createNotification(theme: String, title: String) {
        val i = TrackedFragment.newIntent(this)
        val pi = PendingIntent.getActivity(this, 0, i, 0)

        createChannel()

        val notification = NotificationCompat.Builder(this, TRACKED_CHANNEL_ID)
                .setTicker(getString(R.string.notification_ticker))
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle(getString(R.string.notification_title, theme))
                .setContentText(title)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            val androidChannel = NotificationChannel(TRACKED_CHANNEL_ID, TRACKED_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            androidChannel.enableLights(true)
            androidChannel.enableVibration(true)
            androidChannel.lightColor = Color.DKGRAY
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(androidChannel)
        }
    }
}