package py.com.dianascode.sweatworkschallenge.viewModel

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import py.com.dianascode.sweatworkschallenge.SweatWorks
import py.com.dianascode.sweatworkschallenge.dataModels.UserRepository
import py.com.dianascode.sweatworkschallenge.models.User
import py.com.dianascode.sweatworkschallenge.models.UserResponse
import java.util.concurrent.TimeUnit

class UserViewModel (var application: SweatWorks, context: Context?) : SweatWorksViewModel(context) {

    fun getUsers(results: Int?=null, seed: String?=null, showProgressDialog: Boolean = true): Observable<UserResponse> {
        return UserRepository(application).getUsers(results)
            .debounce(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { if(showProgressDialog) addTask() }
            .doOnNext { if(showProgressDialog) finishTask() }
            .doOnError { if(showProgressDialog) finishTask(); it.printStackTrace() }
    }

    fun getFavoriteUsers(): List<User> {
        return UserRepository(application).getFavoriteUsers(ctx!!)
    }

    fun saveFavoriteUser(user: User) {
        return UserRepository(application).saveFavoriteUser(ctx!!, user)
    }

    fun isFavoriteUser(user: User): Boolean {
        return UserRepository(application).isFavoriteUser(ctx!!, user)
    }

    fun removeFavoriteUser(user: User) {
        return UserRepository(application).removeFavoriteUser(ctx!!, user)
    }
}