package com.meishu.android.itfinder.provider

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.meishu.android.itfinder.model.Post
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class TimePadProvider {

    companion object {
        const val TAG = "TimePad provider"
        //const val API_KEY = ""
        const val ROOT = "https://api.timepad.ru/v1/events"
        const val SOURCE = "https://welcome.timepad.ru/"
    }

    fun getUrlString(url : String) = String(getUrlBytes(url))

    private fun getUrlBytes(urlSource: String): ByteArray {
        val url = URL(urlSource)
        val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
        try {
            val out = ByteArrayOutputStream()
            val input = connection.inputStream
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("${connection.responseMessage}: with $urlSource")
            }

            var bytesRead: Int
            val buffer = ByteArray(1024)
            bytesRead = input.read(buffer)
            while (bytesRead > 0) {
                out.write(buffer, 0, bytesRead)
                bytesRead = input.read(buffer)
            }

            out.close()
            return out.toByteArray()
        } finally {
            connection.disconnect()
        }
    }

    fun fetchItems() : List<Post> {
        val url = Uri.parse(ROOT)
                .buildUpon()
                .appendQueryParameter("limit", "10")
                .appendQueryParameter("sort", "+starts_at")
                .appendQueryParameter("category_ids", "452")
                .build().toString()

        var result : List<Post> = ArrayList()
        try {
            val jsonString = getUrlString(url)
            result = parseJson(jsonString)
            Log.i(TAG, "Got json!")
        } catch (e : Exception) {
            Log.i(TAG, "Failed!")
            e.printStackTrace()
        }
        return result
    }

    fun parseJson(jsonString : String) : List<Post> {
        val jsonBody = JSONObject(jsonString)
        val jsonValues = jsonBody.getJSONArray("values")
        val array = ArrayList<Post>()
        for (i in 0..(jsonValues.length() - 1)) {
            val jsonObject = jsonValues.getJSONObject(i)
            val time = dateStringToLong(jsonObject.getString("starts_at"))
            val title = jsonObject.getString("name")
            val href = jsonObject.getString("url")
            val imageUrl = "https://${jsonObject.getJSONObject("poster_image").getString("uploadcare_url")}"
            val post = Post(title = title, time = time, href = href, imageUrl = imageUrl, source = SOURCE)
            array.add(post)
        }
        return array
    }

    @SuppressLint("SimpleDateFormat")
    fun dateStringToLong(date : String) : Long {
        val df = SimpleDateFormat("yyyy-MM-dd")
        return df.parse(date).time
    }
}