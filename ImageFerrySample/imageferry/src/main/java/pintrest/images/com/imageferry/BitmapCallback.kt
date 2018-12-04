package pintrest.images.com.imageferry

import android.graphics.Bitmap

/**
 * Created by Ishtiaq on Dec , 01, 2018.
 */
interface BitmapCallback {
    fun onFailure(e: Exception)

    fun onBitmapLoaded(tag: String, bitmap: Bitmap)
}