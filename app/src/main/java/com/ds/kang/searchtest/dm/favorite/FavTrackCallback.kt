package com.ds.kang.searchtest.dm.favorite

interface FavTrackCallback<T> {
    fun onLoadFinished(result: T?)
    fun onLoadReset()
}
