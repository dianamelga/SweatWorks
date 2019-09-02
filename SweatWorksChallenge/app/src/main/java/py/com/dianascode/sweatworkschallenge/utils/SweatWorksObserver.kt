package py.com.dianascode.sweatworkschallenge.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import py.com.dianascode.sweatworkschallenge.BuildConfig
import py.com.dianascode.sweatworkschallenge.R
import py.com.dianascode.sweatworkschallenge.models.SweatWorksError
import py.com.dianascode.sweatworkschallenge.views.activities.SweatWorksActivity
import retrofit2.HttpException

abstract class SweatWorksObserver<T>(var context: Context, var cb: (SweatWorksError) -> Unit = {}) : Observer<T> {
    var willShowError = true

    override fun onComplete() {
        if (BuildConfig.DEBUG) Log.d("OBSERVER", "onComplete")
    }

    override fun onSubscribe(d: Disposable) {
        if (BuildConfig.DEBUG) Log.d("OBSERVER", "onSubscribe")
    }

    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) Log.d("OBSERVER", "onError")
        if (willShowError) {
            if (e is HttpException) {

                val error = SweatWorksError.create(e.response().errorBody())
                error?.let {
                        showMessage(it.message)
                } ?: showMessage(context?.getString(R.string.conexion_failed_msg))

            } else {
                showMessage(context?.getString(R.string.conexion_failed_msg))
            }

        }
    }

    private fun showMessage(message: String?) {
        if (context is SweatWorksActivity) {
            AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                .setMessage(message ?: context.getString(R.string.generic_error_msg))
                .setNegativeButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .create().run {
                    this.window?.setWindowAnimations(R.style.AppTheme_Dialog)
                    this
                }.show()
        }
    }
}