package com.singun.coroutines.demo

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

/**
 * Created by singun on 2019-07-30.
 */
private const val LOG_TAG = "coroutines-ForSource"

object ForSource {
    suspend fun test() {
        Log.d(LOG_TAG, "1")
        delay(500)
        repeat(1000) {
            yield()
            Log.d(LOG_TAG, "number count: $it")
        }
        Log.d(LOG_TAG, "2")
    }
}