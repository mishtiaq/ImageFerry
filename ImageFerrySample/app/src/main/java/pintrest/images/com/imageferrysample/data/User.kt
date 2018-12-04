package pintrest.images.com.imageferrysample.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ishtiaq on Dec , 04, 2018.
 */
class User {
    lateinit var id: String
    lateinit var name: String
    @SerializedName("profile_image")
    lateinit var profileImage: ProfileImage
}