package com.cyrus.base_kotlin_mvvm.utils

import com.blankj.utilcode.BuildConfig
import com.google.gson.Gson
import timber.log.Timber

object LoggerUtils {

	private const val TAG = "Parallax"

	private fun getString(vararg objects: Any?): String {
		return objects.foldIndexed("") { index, acc, any ->
			if (index > 0) acc.plus(" ; ").plus(any)
			else any.toString()
		}
	}

	private fun getStringJson(vararg objects: Any?): String {
		val gson = Gson()
		return objects.foldIndexed("") { index, acc, any ->
			if (index > 0) acc.plus(" ; ").plus(gson.toJson(any))
			else gson.toJson(any)
		}
	}

	fun d(tagName: String, s: String?, vararg objects: Any?) {
		Timber.tag("[${TAG}_${tagName}]").d(s, *objects)
	}

	fun e(tagName: String, s: String?, vararg objects: Any?) {
		Timber.tag("[${TAG}_${tagName}]").e(s, *objects)
	}

	fun e(tagName: String, throwable: Throwable?, s: String?, vararg objects: Any?) {
		Timber.tag("[${TAG}_${tagName}]").e(throwable, s, *objects)
	}

	fun i(tagName: String, s: String?, vararg objects: Any?) {
		Timber.tag("[${TAG}_${tagName}]").i(s, *objects)
	}

	fun i(tagName: String, throwable: Throwable?, s: String?, vararg objects: Any?) {
		Timber.tag("[${TAG}_${tagName}]").i(throwable, s, *objects)
	}

	fun init() {
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}

	fun w(s: String?, vararg objects: Any?) {
		Timber.w(s, *objects)
	}

	fun w(throwable: Throwable?, s: String?, vararg objects: Any?) {
		Timber.w(throwable, s, *objects)
	}
}