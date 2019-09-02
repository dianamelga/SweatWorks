package py.com.dianascode.sweatworkschallenge.dataModels

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import py.com.dianascode.sweatworkschallenge.SweatWorks
import py.com.dianascode.sweatworkschallenge.models.User
import py.com.dianascode.sweatworkschallenge.models.UserResponse
import py.com.dianascode.sweatworkschallenge.utils.toJSON


class UserRepository(var application: SweatWorks) {

    fun getUsers(results: Int? = null): Observable<UserResponse> {
        return application.apiRandomUser!!.getUsers(results)
    }

    fun getFavoriteUsers(ctx: Context): List<User> {
        val list: ArrayList<User> = ArrayList()
        val sharedPref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        if (sharedPref.contains(PREF_NAME)) {
            val itemType = object : TypeToken<List<User>>() {}.type
            val usersList = Gson().fromJson<List<User>>(sharedPref.getString(PREF_NAME, ""), itemType)
            list.addAll(usersList)
        }

        return list
    }

    fun saveFavoriteUser(ctx: Context, user: User) {
        val sharedPref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        var usersList: ArrayList<User> = ArrayList()

        if (sharedPref.contains(PREF_NAME)) {
            val itemType = object : TypeToken<ArrayList<User>>() {}.type
            usersList = Gson().fromJson<ArrayList<User>>(sharedPref.getString(PREF_NAME, ""), itemType)
        }

        usersList.add(user)
        editor.putString(PREF_NAME, usersList.toJSON())
        editor.apply()
    }

    private fun updateFavoriteUsersList(ctx: Context, usersList: ArrayList<User>) {
        val sharedPref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putString(PREF_NAME, usersList.toJSON())
        editor.apply()
    }

    fun isFavoriteUser(ctx: Context, user: User): Boolean {
        val users = getFavoriteUsers(ctx)
        for (u in users) {
            if (u.toJSON() == user.toJSON()) {
                return true
            }
        }
        return false
    }

    fun removeFavoriteUser(ctx: Context, user: User) {
        if(isFavoriteUser(ctx, user)) {
            val favorites = getFavoriteUsers(ctx)
            val favoritesNew: ArrayList<User> = ArrayList()
            favoritesNew.addAll(favorites)
            var index = 0
            for (u in favorites) {
                if(u.toJSON() == user.toJSON()) {
                    favoritesNew.removeAt(index)
                    break
                }
                index++
            }

            updateFavoriteUsersList(ctx, favoritesNew)
        }
    }

    companion object {
        const val PRIVATE_MODE = 0
        const val PREF_NAME = "favorite-users"
    }
}