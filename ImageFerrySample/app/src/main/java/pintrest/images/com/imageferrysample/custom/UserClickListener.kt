package pintrest.images.com.imageferrysample.custom

import pintrest.images.com.imageferrysample.data.User

/**
 * Created by Ishtiaq on Dec , 04, 2018.
 */
interface UserClickListener {
    fun onClick(user: User, position: Int)
}