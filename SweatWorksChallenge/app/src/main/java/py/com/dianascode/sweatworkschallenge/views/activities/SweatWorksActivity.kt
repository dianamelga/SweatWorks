package py.com.dianascode.sweatworkschallenge.views.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import py.com.dianascode.sweatworkschallenge.SweatWorks

open class SweatWorksActivity : AppCompatActivity() {
    var sweatWorks: SweatWorks?= null
    var disposeBag: CompositeDisposable = CompositeDisposable()
    private var progressDialog: ProgressDialog? = null
    private val progressReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                SHOW_PROGRESS_ACTION -> showProgress()
                DISMISS_PROGRESS_ACTION -> dismissProgress()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sweatWorks = application as SweatWorks
        progressDialog = ProgressDialog(this)
        progressDialog?.isIndeterminate = true
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage("Please wait...")

        val intentFilter = IntentFilter()
        intentFilter.addAction(SHOW_PROGRESS_ACTION)
        intentFilter.addAction(DISMISS_PROGRESS_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(progressReceiver, intentFilter)
        setupWindowAnimations()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
        disposeBag.clear()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(progressReceiver)
    }

    fun addForDisposing(d: Disposable?) {
        d?.let {
            disposeBag.add(it)
        }
    }

    fun showProgress() {
        progressDialog?.let {
            if (!it.isShowing && !isFinishing) {
                it.show()
            }
        }
    }

    fun dismissProgress() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    private fun setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= 21) {
            val fade = Fade()
            fade.duration = 350
            window.enterTransition = fade

            val slide = Slide()
            slide.duration = 350
            window.exitTransition = slide
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> {
                    super.onBackPressed()
                }
                else -> {
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SHOW_PROGRESS_ACTION = "py.com.dianascode.sweatworkschallenge.SHOW_PROGRESS"
        const val DISMISS_PROGRESS_ACTION = "py.com.dianascode.sweatworkschallenge.DISSMIS_PROGRESS"
    }
}
