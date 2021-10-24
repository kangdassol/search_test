package com.ds.kang.search.itunes.api

import com.ds.kang.search.BaseResponse
import com.ds.kang.search.itunes.obj.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

internal interface ItunesApi {
    @Headers(
        "Accept: application/json; charset=UTF-8",
        "Connection: Close"
    )
    @GET("search")
    fun getSearchResult(
        @Query("term") term: String,
        @Query("entity") entity: String,
        @Query("limit") limit: Int
    ) : Call<BaseResponse<Track>>
}