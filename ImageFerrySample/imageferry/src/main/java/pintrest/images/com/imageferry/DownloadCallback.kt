package pintrest.images.com.imageferry

/**
 * Created by Ishtiaq on Dec , 01, 2018.
 */
interface DownloadCallback {
    fun onFailure(tag: String, e: Exception)
    fun onResponse(tag: String, data: ByteArray)
}