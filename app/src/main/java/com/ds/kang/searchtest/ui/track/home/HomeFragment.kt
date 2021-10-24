package com.ds.kang.searchtest.ui.track.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ds.kang.search.TaskProvider
import com.ds.kang.search.itunes.ItunesDataManager
import com.ds.kang.search.itunes.obj.Track
import com.ds.kang.search.itunes.obj.TrackInfo
import com.ds.kang.searchtest.R
import com.ds.kang.searchtest.dm.favorite.FavTrackLoader
import com.ds.kang.searchtest.dm.favorite.FavTrackManager
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.dm.favorite.provider.FavTrackProvider
import com.ds.kang.searchtest.ui.track.TrackContract
import com.ds.kang.searchtest.ui.track.TrackItemDecoration
import com.ds.kang.searchtest.util.Logger
import com.ds.kang.searchtest.util.NetworkChecker

class HomeFragment() : Fragment(), TrackContract.TrackView<TrackInfo> {
    private val TAG = HomeFragment::class.simpleName!!

    private val SEARCH_KEY_WOARD = "greenday"
    private val SEARCH_ENTITY = "song"
    private val SEARCH_LIMIT_COUNT = 30

    private val itunesDataManager: ItunesDataManager = ItunesDataManager(this)

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var trackInfoListAdapter: TrackListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Logger.INCLUDE) {
            Logger.d(TAG, "onViewCreated")
        }

        if (NetworkChecker.isConnectedNetwork(requireContext())) {
            initView(view)
        } else {
            view.findViewById<ConstraintLayout>(R.id.network_connection_fail_layout).visibility =
                View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LoaderManager.getInstance(this).destroyLoader(FavTrackLoader.LOADER_MAIN)
        itunesDataManager.release()
    }

    private fun initView(view: View) {
        if (Logger.INCLUDE) {
            Logger.d(TAG, "initView")
        }

        val favTrackManager =
            FavTrackManager<FavTrackInfo>(FavTrackProvider.URI_ITEM, requireContext())
        val homeTrackPresenter = HomeTrackPresenter(favTrackManager)
        val trackInfoList: ArrayList<TrackInfo> = ArrayList()
        homeTrackPresenter.loadFavList(LoaderManager.getInstance(this)) {
            loadTrackList(trackInfoList)
        }

        val trackInfoListView: RecyclerView = view.findViewById(R.id.track_info_list_view)
        val rect =
            Rect(0, 0, 0, resources.getDimension(R.dimen.track_row_item_vertical_spacing).toInt())
        trackInfoListView.addItemDecoration(TrackItemDecoration(rect))
        trackInfoListView.layoutManager = LinearLayoutManager(context)
        trackInfoListAdapter = TrackListAdapter(trackInfoList, homeTrackPresenter)
        trackInfoListView.adapter = trackInfoListAdapter

        swipeRefreshLayout = view.findViewById(R.id.track_swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            if (!swipeRefreshLayout.isRefreshing) {
                refreshView(trackInfoList)
            } else {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun refreshView(trackInfoList: ArrayList<TrackInfo>) {
        if (Logger.INCLUDE) {
            Logger.d(TAG, "refreshView")
        }

        loadTrackList(trackInfoList)
    }

    private fun loadTrackList(trackInfoList: ArrayList<TrackInfo>) {
        if (Logger.INCLUDE) {
            Logger.d(TAG, "loadTrackList")
        }

        itunesDataManager.getSearchResult(
            SEARCH_KEY_WOARD,
            SEARCH_ENTITY,
            SEARCH_LIMIT_COUNT,
            object : TaskProvider<Track>() {
                override fun onPreTask() {
                    super.onPreTask()
                    if (Logger.INCLUDE) {
                        Logger.d(TAG, "getSearchResult onPreTask")
                    }
                }

                override fun onError(code: Int, message: String) {
                    super.onError(code, message)
                    if (Logger.INCLUDE) {
                        Logger.d(TAG, "getSearchResult onError : ${code}, message : ${message}")
                    }

                    swipeRefreshLayout.isRefreshing = false
                    showErrorToast(code, message)
                }

                override fun onPostTask(result: Track?) {
                    if (Logger.INCLUDE) {
                        Logger.d(TAG, "getSearchResult result : ${result?.resultCount}")
                    }

                    result?.let {
                        trackInfoList.clear()
                        trackInfoList.addAll(it.results)
                        trackInfoListAdapter.notifyDataSetChanged()
                    }

                    swipeRefreshLayout.isRefreshing = false
                }
            })
    }

    private fun showErrorToast(code: Int, message: String) {
        val errorMessage = "code : $code, message : $message"
        Toast.makeText(requireActivity().applicationContext, errorMessage, Toast.LENGTH_LONG)
            .show()
    }

    companion object {
        val CLASS_NAME = HomeFragment::class.java.canonicalName
    }
}