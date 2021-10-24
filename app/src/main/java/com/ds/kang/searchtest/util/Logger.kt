package com.ds.kang.searchtest.util

import android.util.Log

class Logger {
    companion object {
        const val INCLUDE = true

        fun d(tag: String, log: String) {
            if (INCLUDE) {
                Log.d(tag, log)
            }
        }
    }
}