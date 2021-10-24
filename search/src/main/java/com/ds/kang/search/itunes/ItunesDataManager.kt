package com.ds.kang.search.itunes

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.lifecycle.*
import com.ds.kang.search.OKHttpGenerator
import com.ds.kang.search.TaskProvider
import com.ds.kang.search.itunes.module.ItunesModule
import com.ds.kang.search.itunes.obj.Track
import com.ds.kang.search.itunes.remote.ItunesRemote
import retrofit2.Retrofit

class ItunesDataManager(val lifecycleOwner: LifecycleOwner) : LifecycleObserver {
    private val TAG = ItunesDataManager::class.java.simpleName!!
    private val ituensRemote: ItunesRemote
    private var workThread: HandlerThread? = null
    private lateinit var workHandler:Handler
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        val okHttpClient = OKHttpGenerator().getUnsafeOkHttpClient()
        val itunesModule = ItunesModule()
        val itunesRetrofit: Retrofit = itunesModule.provideItunesModule(okHttpClient)
        ituensRemote = ItunesRemote(itunesModule.provideItunesApi(itunesRetrofit))

        lifecycleOwner.lifecycle.addObserver(this)

        start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {
        if (workThread == null) {
            workThread = HandlerThread("SearchThread")
            workThread?.start()
            workHandler = Handler(workThread!!.looper)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun quit() {
        workHandler.removeCallbacksAndMessages(null)
        workThread?.quitSafely()
        workThread = null
    }

    fun release() {
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    fun getSearchResult(
        term: String,
        entity: String,
        limit: Int,
        taskProviderProvider: TaskProvider<Track>
    ) {
        taskProviderProvider.onPreTask()
        workHandler.post {
            val response = ituensRemote.getSearchResult(term, entity, limit)
            if(workThread?.isInterrupted == true){
                return@post
            }
            if (response?.isSuccessful() == true) {
                mainHandler.post {
                    taskProviderProvider.onPostTask(response.content)
                }
            } else {
                mainHandler.post {
                    taskProviderProvider.onError(
                        response?.code ?: 0,
                        response?.message ?: ""
                    )
                }
            }
        }
    }
}