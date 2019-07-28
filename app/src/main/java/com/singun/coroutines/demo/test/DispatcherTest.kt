package com.singun.coroutines.demo.test

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Created by singun on 2019/7/28 0028.
 */
object DispatcherTest {
    suspend fun test(view: View) {
        withContext(Dispatchers.IO) {
            val result = getResult()

            withContext(Dispatchers.Main) {
                Snackbar.make(view, result, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * 模拟耗时操作，实际上delay是不会真的耗时，即使在主线程操作也是可以的
     */
    private suspend fun getResult(): String {
        var count = 0
        delay(800)
        count++
        return "result is $count"
    }
}