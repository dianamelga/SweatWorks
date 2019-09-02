package py.com.dianascode.sweatworkschallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import py.com.dianascode.sweatworkschallenge.views.activities.HomeActivity
import py.com.dianascode.sweatworkschallenge.views.activities.SweatWorksActivity
/*
    this activity can be use as an SplashActivity but for now just call to HomeActivity
 */
class MainActivity : SweatWorksActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
}
