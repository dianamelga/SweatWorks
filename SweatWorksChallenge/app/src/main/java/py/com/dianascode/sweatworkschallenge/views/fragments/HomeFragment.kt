package py.com.dianascode.sweatworkschallenge.views.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

import py.com.dianascode.sweatworkschallenge.R
import py.com.dianascode.sweatworkschallenge.adapters.UserAdapter
import py.com.dianascode.sweatworkschallenge.models.User
import py.com.dianascode.sweatworkschallenge.models.UserResponse
import py.com.dianascode.sweatworkschallenge.utils.SweatWorksObserver
import py.com.dianascode.sweatworkschallenge.viewModel.UserViewModel
import py.com.dianascode.sweatworkschallenge.views.activities.UserDetailActivity

class HomeFragment : SweatWorksFragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var usersAdapter: UserAdapter
    private lateinit var favUsersAdapter: UserAdapter
    private var usersList: ArrayList<User> = ArrayList()
    private var favUsersList: ArrayList<User> = ArrayList()
    private var usersCachedList: ArrayList<User> = ArrayList()
    private var favUsersCachedList: ArrayList<User> = ArrayList()
    private var callback: (User) -> Unit= {}

    private var isLoading = false
    private val limit = 100

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var favUsersLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        userViewModel = UserViewModel(sweatWorks!!, sweatWorksActivity!!)
        layoutManager =  GridLayoutManager(sweatWorksActivity!!, 6)
        favUsersLayoutManager = LinearLayoutManager(sweatWorksActivity!!, LinearLayoutManager.HORIZONTAL, false)

        callback = {
            val i = Intent(sweatWorksActivity!!, UserDetailActivity::class.java)
            i.putExtra(UserDetailActivity.USER_SELECTED, it)
            startActivityForResult(i, REQUEST_CODE_USER_DETAIL)
        }

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = UserAdapter(usersList, sweatWorksActivity!!)
        usersAdapter.callback = {
            callback(it)
        }
        rvListUsers.layoutManager = layoutManager
        rvListUsers.adapter = usersAdapter


        rvListUsers.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //recyclerView got scrolled vertically
                if(dy > 0) {
                    //check if we have reached the bottom of the recyclerView or not
                    //to do that we need to know how many items are there in the screen
                    //what is the top item position
                    //an recyclerView size
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = usersAdapter.itemCount

                    //if is not loading, we can get the next page data
                    if(!isLoading) {
                        //check if we reached the bottom or not
                        if((visibleItemCount + pastVisibleItem) >= total) {
                            getUsersList()
                        }
                    }


                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        getUsersList()

        favUsersAdapter = UserAdapter(favUsersList, sweatWorksActivity!!)
        favUsersAdapter.callback = {
            callback(it)
        }
        rvFavUsers.layoutManager = favUsersLayoutManager
        rvFavUsers.adapter = favUsersAdapter
        getFavoriteUsers()

    }

    private fun getFavoriteUsers() {
        favUsersProgressBar.visibility = View.VISIBLE
        favUsersList.clear()
        favUsersList.addAll(userViewModel.getFavoriteUsers())
        favUsersCachedList.clear()
        favUsersCachedList.addAll(favUsersList)
        favUsersAdapter.notifyDataSetChanged()
        favUsersProgressBar.visibility = View.GONE

        if(favUsersList.size > 0) {
            llFavUsers.visibility = View.VISIBLE
        }else{
            llFavUsers.visibility = View.GONE
        }
    }


    private fun getUsersList() {
        isLoading = true
        progressBar.visibility = View.VISIBLE


        //isn't necessary to show progressDialog because we're already showing the progressBar
        userViewModel.getUsers(limit, showProgressDialog = false).subscribe(object : SweatWorksObserver<UserResponse>(sweatWorksActivity!!) {
            override fun onNext(t: UserResponse) {
                t.results?.let {
                    usersList.addAll(it)
                    usersCachedList.addAll(it)
                    usersAdapter.notifyDataSetChanged()
                }

                isLoading = false
                progressBar.visibility = View.GONE
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                isLoading = false
                progressBar.visibility = View.GONE
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_USER_DETAIL && resultCode == RESULT_OK) {
            getFavoriteUsers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_search, menu)
        (menu!!.findItem(R.id.action_search).actionView as SearchView).apply {
            this.queryHint= "Search"
            setIconifiedByDefault(false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    p0?.let { s ->
                        val temp = ArrayList<User>()
                        val favTemp = ArrayList<User>()
                        if (s.isNotBlank() || s.isNotEmpty()) {
                            for (item in usersCachedList) {
                                if(item.name?.first?.toLowerCase()?.contains(s.toLowerCase()) == true ||
                                    item.name?.last?.toLowerCase()?.contains(s.toLowerCase()) == true) {
                                    temp.add(item)
                                }
                            }

                            for(item in favUsersCachedList) {
                                if(item.name?.first?.toLowerCase()?.contains(s.toLowerCase()) == true ||
                                    item.name?.last?.toLowerCase()?.contains(s.toLowerCase()) == true) {
                                    favTemp.add(item)
                                }
                            }
                        } else {
                            temp.addAll(usersCachedList)
                            favTemp.addAll(favUsersCachedList)
                        }
                        usersList.clear()
                        usersList.addAll(temp)
                        usersAdapter.notifyDataSetChanged()

                        favUsersList.clear()
                        favUsersList.addAll(favTemp)
                        favUsersAdapter.notifyDataSetChanged()
                    }
                    return false
                }
            })
        }
    }

    companion object {
        const val REQUEST_CODE_USER_DETAIL = 101
        fun newInstance(b: Bundle? = null, callback:(User) -> Unit={}): SweatWorksFragment {
            val fragment = HomeFragment()
            b?.let { fragment.arguments = it }
            fragment.callback = callback
            return fragment
        }


    }


}
