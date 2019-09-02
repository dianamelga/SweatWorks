package py.com.dianascode.sweatworkschallenge.viewModel

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import py.com.dianascode.sweatworkschallenge.utils.ActivityTracker
import py.com.dianascode.sweatworkschallenge.views.activities.SweatWorksActivity

open class SweatWorksViewModel(var ctx : Context?) {
    private val activityTracker: ActivityTracker =  ActivityTracker.instance

    init {
        activityTracker.counter.subscribe {
            if(it <= 0) {
                dismissProgress()
            }
        }
    }

    fun showProgress() {
        ctx?.let {
            var intent = Intent(SweatWorksActivity.SHOW_PROGRESS_ACTION)
            LocalBroadcastManager.getInstance(it)
                .sendBroadcast(intent)
        }
    }

    fun dismissProgress() {
        ctx?.let {
            var intent = Intent(SweatWorksActivity.DISMISS_PROGRESS_ACTION)
            LocalBroadcastManager.getInstance(it)
                .sendBroadcast(intent)
        }
    }

    fun addTask() {
        var counter = activityTracker.counter.value
        if (counter == 0) {
            showProgress()
        }
        counter++
        activityTracker.counter.onNext(counter)
    }

    fun finishTask() {
        var counter = activityTracker.counter.value
        counter--
        activityTracker.counter.onNext(counter)
    }

}