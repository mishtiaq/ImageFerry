package pintrest.images.com.imageferry

import okhttp3.*
import java.io.IOException
import java.util.HashSet

/**
 * Created by Ishtiaq on Dec , 01, 2018.
 */
open class Downloader {
    private val callMap = HashSet<String>()
    private val client = OkHttpClient()

    fun download(url: String, callback: DownloadCallback) {
        if (callMap.contains(url)) {
            return
        }
        val data = CacheManager.getInstance().getObjectFromMemCache(url)
        if (data != null) {
            callback.onResponse(url, data)
            return
        }
        callMap.add(url)

        val request = Request.Builder().url(url).build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callMap.remove(url)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        val bytes = if (response.body() != null) response.body()!!.bytes() else null
                        if (bytes != null) {
                            CacheManager.getInstance().addDataMemoryCache(url, bytes)
                            callback.onResponse(url, bytes)
                        } else {
                            callback.onFailure(url, NullPointerException())
                        }
                    } catch (e: Exception) {
                        callback.onFailure(url, e)
                    }

                }
                callMap.remove(url)
            }
        })

    }

    fun cancel(url: String) {
        if (callMap.remove(url)) {
            var calls = client.dispatcher().runningCalls()
            if (cancelCall(url, calls))
                return

            calls = client.dispatcher().queuedCalls()
            cancelCall(url, calls)
        }
    }

    private fun cancelCall(url: String, calls: List<Call>): Boolean {
        for (call in calls) {
            if (call.request().url().toString() == url) {
                call.cancel()
                return true
            }
        }
        return false
    }
}