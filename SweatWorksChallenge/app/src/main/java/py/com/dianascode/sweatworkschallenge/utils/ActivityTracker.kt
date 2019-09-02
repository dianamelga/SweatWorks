package py.com.dianascode.sweatworkschallenge.utils

import io.reactivex.subjects.BehaviorSubject

class ActivityTracker private constructor() {
    val counter : BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

    private object Holder { val INSTANCE = ActivityTracker() }

    companion object {
        val instance : ActivityTracker by lazy { Holder.INSTANCE }
    }
}