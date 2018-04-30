package com.meishu.android.itfinder.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import org.jsoup.Jsoup


class ThumbnailDownloader<T>(var responseHandler: Handler) : HandlerThread(TAG) {

    companion object {
        const val TAG = "Thumbnail downloader"
        var MESSAGE_DOWNLOAD = 0
    }

    private var hasQuit = false
    private lateinit var requestHandler : Handler
    lateinit var thumbnailListener : ThumbnailDownloadListener<T>
    private val requestMap = ConcurrentHashMap<T, String>()

    interface ThumbnailDownloadListener<in T> {
        fun onThumbnailDownloaded(target : T, thumbnail : Bitmap)
    }

    override fun onLooperPrepared() {
        requestHandler = object : Handler() {

            override fun handleMessage(msg : Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target : T = msg.obj as T
                    Log.i(TAG, "Request from URL: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }


    fun queueThumbnail(target : T, url : String) {
        Log.i(TAG, "Got an url: $url")

        if (url == "") {
            requestMap.remove(target)
        } else {
            requestMap[target] = url
            requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
        }
    }

    fun clearQueue() {
        requestHandler.removeMessages(MESSAGE_DOWNLOAD)
        requestMap.clear()
    }

    fun handleRequest(target : T) {
        val url : String = requestMap[target] ?: ""

        if (url == "") {
            return
        }

        val bitmapBytes : ByteArray = getBytes(url) ?: return
        val bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
        Log.i(TAG, "BITMAP CREATED")
        responseHandler.post {
            if (requestMap[target] != url || hasQuit) {
                return@post
            }

            requestMap.remove(target)
            thumbnailListener.onThumbnailDownloaded(target, bitmap)
        }
    }

    fun getBytes(urlSource : String): ByteArray? {
        return try {
            val resultImageResponse = Jsoup.connect(urlSource)
                    .ignoreContentType(true).execute()
            resultImageResponse.bodyAsBytes()
        } catch (e : Exception) {
            e.printStackTrace()
            null
        }
    }
}