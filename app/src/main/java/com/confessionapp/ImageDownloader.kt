package com.confessionapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

class ImageDownloader : AsyncTask<String?, Void?, Bitmap?>() {
    override fun doInBackground(vararg urls: String?): Bitmap? {
        return try {
            val url = URL(urls[0])
            val connection =
                url.openConnection() as HttpURLConnection
            connection.connect()
            val `in` = connection.inputStream
            BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}