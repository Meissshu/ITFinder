package com.meishu.android.itfinder.provider

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.meishu.android.itfinder.data.DataCenter
import com.meishu.android.itfinder.model.Post
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class TimePadProvider : Provider {

    companion object {
        const val TAG = "TimePad provider"
        //const val API_KEY = ""
        const val ROOT = "https://api.timepad.ru/v1/events"
        const val SOURCE = "https://welcome.timepad.ru/"
        private const val SORT_BY = "+starts_at"
        private const val CATEGORY = "452"
        private const val FIELDS = "location"
        //var ENDPOINT: Uri = Uri()
    }

    private fun getEndpoint(limit: Int = DataCenter.DEFAULT_THRESHOLD, sortBy: String = SORT_BY, category: String = CATEGORY, fieldsToShow: String = FIELDS) =
            Uri.parse(ROOT)
                    .buildUpon()
                    .appendQueryParameter("limit", Integer.toString(limit))
                    .appendQueryParameter("sort", sortBy)
                    .appendQueryParameter("category_ids", category)
                    .appendQueryParameter("fields", fieldsToShow)
                    .build()


    override fun fetchPosts(threshold: Int): List<Post> {
        val url = buildUrl(null, threshold)
        return downloadPosts(url)
    }

    override fun searchPosts(query: String, threshold: Int): List<Post> {
        val url = buildUrl(query, threshold)
        return downloadPosts(url)
    }

    private fun downloadPosts(url: String): List<Post> {

        var result: List<Post> = ArrayList()
        try {
            val jsonString = getUrlString(url)
            result = parseJson(jsonString)
            Log.i(TAG, "Got json!")
        } catch (e: Exception) {
            Log.i(TAG, "Failed!")
            e.printStackTrace()
        }
        return result
    }

    private fun buildUrl(query: String?, threshold: Int): String {
        val uriBuilder = getEndpoint(threshold).buildUpon()
        if (query != null) {
            uriBuilder.appendQueryParameter("keywords", prepareQueryString(query))
        }

        return uriBuilder.toString()
    }

    private fun prepareQueryString(query: String): String = query.replace(" ", ",")

    private fun parseJson(jsonString: String): List<Post> {
        val jsonBody = JSONObject(jsonString)
        val jsonValues = jsonBody.getJSONArray("values")
        val array = ArrayList<Post>()
        for (i in 0..(jsonValues.length() - 1)) {
            val jsonObject = jsonValues.getJSONObject(i)
            val time = dateStringToLong(jsonObject.getString("starts_at"))
            val title = jsonObject.getString("name")
            val href = jsonObject.getString("url")
            val imageUrl = "https://${jsonObject.getJSONObject("poster_image").getString("uploadcare_url")}"
            val place = parseLocation(jsonObject)
            val post = Post(title = title, time = time, href = href, imageUrl = imageUrl, source = SOURCE, place = place)
            array.add(post)
        }
        return array
    }

    private fun parseLocation(jsonObject: JSONObject): String {
        val stringBuilder = StringBuilder("")
        try {
            val locationObject = jsonObject.getJSONObject("location")

            var string = locationObject.getString("country")
            stringBuilder.append(string)

            string = locationObject.getString("city")
            stringBuilder.append(", ").append(string)

            string = locationObject.getString("address")
            stringBuilder.append(", ").append(string)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    private fun getUrlString(url: String) = String(getUrlBytes(url))

    private fun getUrlBytes(urlSource: String): ByteArray {
        val url = URL(urlSource)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
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

    @SuppressLint("SimpleDateFormat")
    private fun dateStringToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy-MM-dd")
        return df.parse(date).time
    }
}