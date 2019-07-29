package com.singun.coroutines.demo.test

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

/**
 * Created by singun on 2019-07-29.
 */
private const val LOG_TAG = "coroutines-ScopeTest"

object ScopeTest {
    suspend fun test(view: View) {
        // GlobalScope是全局的Scope，没有生命周期限制，由它执行的Scope需要自行维护
        val jobParent = GlobalScope.launch(Dispatchers.Default) {
            val jobChild = launch {
                try {
                    delay(500)
                } catch (e: CancellationException) {
                    Log.d(LOG_TAG, "cancel $e")
                }
                // 代码中如果没有delay之类的操作，需要用isActive来判断，用于正常退出cancel的协程
                Snackbar.make(view, "jobChild, active: $isActive", Snackbar.LENGTH_LONG).show()
            }
        }
//        delay(300)
//        jobParent.cancel()
    }
}