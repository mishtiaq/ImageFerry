package pintrest.images.com.imageferrysample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import pintrest.images.com.imageferry.ImageLoader
import pintrest.images.com.imageferrysample.*
import pintrest.images.com.imageferrysample.custom.EndlessRecyclerViewScrollListener
import pintrest.images.com.imageferrysample.custom.UserClickListener
import pintrest.images.com.imageferrysample.data.User
import pintrest.images.com.imageferrysample.data.UserWrapper
import pintrest.images.com.imageferrysample.ui.main.adapter.ProfileAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var adapter: ProfileAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        swipeContainer.isRefreshing = true
        viewModel.loadData()

        initRecyclerView()
        setListeners()

        viewModel.userData.observe(this, Observer<ArrayList<UserWrapper>> { list ->
            swipeContainer.isRefreshing = false
            if (list == null) {
                view?.let { Snackbar.make(it, R.string.data_loading_failed, Snackbar.LENGTH_SHORT).show() }
                return@Observer
            }

            if (adapter == null) {
                adapter = ProfileAdapter(
                    list,
                    object : UserClickListener {
                        @Override
                        override fun onClick(user: User, position: Int) {
                            //Load from cache, If not already loaded then call is must be intiated
                            ImageLoader.getInstance().load(ivDetail, user.profileImage.medium)

                            //Loading to show full image
                            ImageLoader.getInstance().load(ivDetail, user.profileImage.large)

                            ivDetail.visibility = View.VISIBLE
                            ivDetail.bringToFront()
                        }
                    })
                rvItems.adapter = adapter
            } else {
                val itemCount = adapter!!.itemCount
                adapter!!.notifyItemInserted(itemCount)
            }
        })

    }

    private fun initRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        rvItems.layoutManager = staggeredGridLayoutManager
        val listener = object : EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Snackbar.make(view, R.string.requesting_data, Snackbar.LENGTH_SHORT).show()
                viewModel.loadData()
            }
        }
        rvItems.addOnScrollListener(listener)
    }

    private fun setListeners() {
        ivDetail.setOnClickListener {
            ivDetail.setImageBitmap(null)
            ivDetail.visibility = View.GONE
        }

        swipeContainer.setOnRefreshListener {
            viewModel.clear()
            view?.let { Snackbar.make(it, R.string.loading_data, Snackbar.LENGTH_SHORT).show() }
            viewModel.loadData()
        }
    }
}
