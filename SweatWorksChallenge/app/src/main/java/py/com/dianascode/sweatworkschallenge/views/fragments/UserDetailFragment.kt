package py.com.dianascode.sweatworkschallenge.views.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_detail.*
import py.com.dianascode.sweatworkschallenge.R
import py.com.dianascode.sweatworkschallenge.models.User
import py.com.dianascode.sweatworkschallenge.utils.UtilTools
import py.com.dianascode.sweatworkschallenge.utils.asFormattedDate
import py.com.dianascode.sweatworkschallenge.viewModel.UserViewModel
import py.com.dianascode.sweatworkschallenge.views.activities.UserDetailActivity

class UserDetailFragment : SweatWorksFragment() {

    private var user: User? = null
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        userViewModel = UserViewModel(sweatWorks!!, sweatWorksActivity!!)
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user?.let {
            with(it) {
                tvTitle.text = name?.title?.capitalize()
                tvName.text = name?.first?.capitalize() + " " + name?.last?.capitalize()

                tvMail.text = email
                tvPhone.text = phone
                tvAddress.text = location?.city + " - " + location?.state + " / " + location?.street
                tvDob.text = dob?.date?.asFormattedDate()

                Picasso.get()
                    .load(picture?.large)
                    .into(ivUser)

                if ("female" == gender?.toLowerCase()) {
                    ivUser.borderColor = ContextCompat.getColor(sweatWorksActivity!!, R.color.pinkFemale)
                } else {
                    ivUser.borderColor = ContextCompat.getColor(sweatWorksActivity!!, R.color.blueMale)
                }

                clPhone.setOnClickListener {
                    if (!UtilTools.hasPermission(Manifest.permission.WRITE_CONTACTS, sweatWorksActivity!!)) {
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_CONTACTS),
                            PERMISSION_REQUEST_CODE_WRITE_CONTACTS
                        )
                    } else {
                        addAsContactConfirmed(this)
                    }
                }


                val fab = view.findViewById<FloatingActionButton>(R.id.btFavorite)
                if(userViewModel.isFavoriteUser(this))
                    fab.setImageDrawable(ContextCompat.getDrawable(sweatWorksActivity!!, R.drawable.ic_heart_pink))

                fab.setOnClickListener {
                    if(!userViewModel.isFavoriteUser(this)) {
                        Toast.makeText(sweatWorksActivity!!, "Saved as a Favorite User!", Toast.LENGTH_SHORT).show()
                        userViewModel.saveFavoriteUser(this)
                        fab.setImageDrawable(ContextCompat.getDrawable(sweatWorksActivity!!, R.drawable.ic_heart_pink))
                    }else{
                        Toast.makeText(sweatWorksActivity!!, "Removed from Favorite Users List", Toast.LENGTH_SHORT).show()
                        userViewModel.removeFavoriteUser(this)
                        fab.setImageDrawable(ContextCompat.getDrawable(sweatWorksActivity!!, R.drawable.ic_heart_pink_outlined))
                    }

                    getUserDetailActivity().setResult(RESULT_OK)

                }

            }
        }

    }

    private fun getUserDetailActivity(): UserDetailActivity {
        return (sweatWorksActivity as UserDetailActivity)
    }

    private fun addAsContactConfirmed(person: User) {

        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = ContactsContract.Contacts.CONTENT_TYPE
        intent.putExtra(
            ContactsContract.Intents.Insert.NAME, person.name?.first?.toLowerCase()?.capitalize() +
                    " " + person.name?.last?.toLowerCase()?.capitalize()
        )
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, person.phone)
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, person.email)

        startActivity(intent)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE_WRITE_CONTACTS -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        user?.let { addAsContactConfirmed(it) }
                    }
                }
            }
        }
    }


    companion object {
        const val PERMISSION_REQUEST_CODE_WRITE_CONTACTS = 100
        fun newInstance(b: Bundle? = null, user: User): SweatWorksFragment {
            val fragment = UserDetailFragment()
            b?.let { fragment.arguments = it }
            fragment.user = user
            return fragment
        }


    }

}
