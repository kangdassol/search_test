package com.ds.kang.search.itunes.remote

import com.ds.kang.search.BaseResponse
import com.ds.kang.search.itunes.api.ItunesApi
import com.ds.kang.search.itunes.obj.Track
import retrofit2.Response

internal class ItunesRemote(private val itunesApi: ItunesApi) {

    fun getSearchResult(term: String, entity: String, limit: Int) : BaseResponse<Track>? {

        val call = itunesApi.getSearchResult(term, entity, limit)
        val response: Response<BaseResponse<Track>> = call.execute()
        return response.body()
    }
}