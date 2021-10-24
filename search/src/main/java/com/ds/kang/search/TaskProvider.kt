package com.ds.kang.search

abstract class TaskProvider<T> {
    open fun onPreTask() {}
    abstract fun onPostTask(result: T?)
    open fun onError(code: Int, message: String) {}
}