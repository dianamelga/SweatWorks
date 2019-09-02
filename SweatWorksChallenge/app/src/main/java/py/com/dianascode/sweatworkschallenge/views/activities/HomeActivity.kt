package py.com.dianascode.sweatworkschallenge.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import py.com.dianascode.sweatworkschallenge.R
import py.com.dianascode.sweatworkschallenge.views.fragments.HomeFragment
import py.com.dianascode.sweatworkschallenge.views.fragments.UserDetailFragment

class HomeActivity : SweatWorksActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "SweatWorks Challenge"
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val fragment = HomeFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, fragment)
            .commit()
    }

}
