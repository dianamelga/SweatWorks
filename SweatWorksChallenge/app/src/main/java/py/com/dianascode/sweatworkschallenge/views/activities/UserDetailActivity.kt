package py.com.dianascode.sweatworkschallenge.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import py.com.dianascode.sweatworkschallenge.R
import py.com.dianascode.sweatworkschallenge.models.User
import py.com.dianascode.sweatworkschallenge.views.fragments.UserDetailFragment

class UserDetailActivity : SweatWorksActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.title = "SweatWorks Challenge"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val userDetailFragment = UserDetailFragment.newInstance(null, intent?.getSerializableExtra(USER_SELECTED) as User)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, userDetailFragment)
            .commit()
    }

    companion object {
        const val USER_SELECTED = "user_selected"
    }
}
