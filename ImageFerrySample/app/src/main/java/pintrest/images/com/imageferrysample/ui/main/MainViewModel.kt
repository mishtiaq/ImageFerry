package pintrest.images.com.imageferrysample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import pintrest.images.com.imageferrysample.data.UserWrapper
import java.io.IOException


open class MainViewModel : ViewModel() {

    val userData: MutableLiveData<ArrayList<UserWrapper>> by lazy {
        MutableLiveData<ArrayList<UserWrapper>>()
    }

    private val client = OkHttpClient()

    fun loadData() {
        val call = client.newCall(Request.Builder().url("http://www.pastebin.com/raw/wgkJgazE").build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                userData.postValue(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val data: String = response.body()?.string().toString()
                    val type = object : TypeToken<ArrayList<UserWrapper>>() {}.type
                    val list = Gson().fromJson<ArrayList<UserWrapper>>(data, type)
                    var value = userData.value
                    if (value == null) {
                        value = ArrayList()
                    }
                    value.addAll(list)
                    userData.postValue(value);
                }
            }
        })
    }

    fun clear() {
        userData.value = ArrayList()
        userData.postValue(userData.value)
    }
}
