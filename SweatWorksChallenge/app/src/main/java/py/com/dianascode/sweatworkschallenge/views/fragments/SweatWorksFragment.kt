package py.com.dianascode.sweatworkschallenge.views.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import py.com.dianascode.sweatworkschallenge.SweatWorks
import py.com.dianascode.sweatworkschallenge.views.activities.SweatWorksActivity

open class SweatWorksFragment : Fragment() {
    var sweatWorksActivity: SweatWorksActivity?= null
    var sweatWorks: SweatWorks?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sweatWorksActivity = activity as SweatWorksActivity
        sweatWorks = activity?.application as SweatWorks
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}
