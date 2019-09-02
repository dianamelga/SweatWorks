package py.com.dianascode.sweatworkschallenge

import androidx.multidex.MultiDexApplication
import py.com.dianascode.sweatworkschallenge.comm.RandomUserApi

class SweatWorks: MultiDexApplication() {
    var apiRandomUser: RandomUserApi?= null

    override fun onCreate() {
        super.onCreate()
        val url = "https://randomuser.me/"
        apiRandomUser = RandomUserApi.createForApi(url)
    }
}