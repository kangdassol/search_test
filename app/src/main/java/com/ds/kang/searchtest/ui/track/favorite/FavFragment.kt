package com.ds.kang.searchtest.ui.track.favorite

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ds.kang.searchtest.R
import com.ds.kang.searchtest.dm.favorite.FavTrackLoader
import com.ds.kang.searchtest.dm.favorite.FavTrackManager
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.dm.favorite.provider.FavTrackProvider
import com.ds.kang.searchtest.ui.track.TrackItemDecoration
import com.ds.kang.searchtest.util.Logger

class FavFragment() : Fragment() {
    private val TAG = FavFragment::class.simpleName!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Logger.INCLUDE) {
            Logger.d(TAG, "onViewCreated")
        }

        initView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LoaderManager.getInstance(this).destroyLoader(FavTrackLoader.LOADER_MAIN)
    }

    private fun initView(view: View) {
        if (Logger.INCLUDE) {
            Logger.d(TAG, "initView")
        }

        val favTrackManager =
            FavTrackManager<FavTrackInfo>(FavTrackProvider.URI_ITEM, requireContext())
        val favTrackPresenter = FavTrackPresenter(favTrackManager)
        val trackInfoListView = view.findViewById<RecyclerView>(R.id.fav_info_list_view)

        val rect =
            Rect(0, 0, 0, resources.getDimension(R.dimen.track_row_item_vertical_spacing).toInt())
        trackInfoListView.addItemDecoration(TrackItemDecoration(rect))
        trackInfoListView.layoutManager = LinearLayoutManager(context)

        favTrackPresenter.loadFavList(LoaderManager.getInstance(this)) {
            trackInfoListView.adapter = FavTrackListAdapter(favTrackPresenter)
        }
    }

    companion object {
        val CLASS_NAME = FavFragment::class.java.canonicalName
    }
}