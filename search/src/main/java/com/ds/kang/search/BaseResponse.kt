package com.ds.kang.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
internal class BaseResponse<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
) : Parcelable {

    @SerializedName("content")
    val content: T? = null


    fun isSuccessful(): Boolean {
        return code in 200..299
    }

}