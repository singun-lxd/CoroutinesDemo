package com.singun.coroutines.demo.test

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.RuntimeException

/**
 * Created by singun on 2019-07-29.
 */
private const val LOG_TAG = "coroutines-ScopeTest"

object ScopeTest {
    suspend fun test(view: View) {
//        testParent(view)
        testAsync(view)
    }

    private suspend fun testParent(view: View) {
        // GlobalScope是全局的Scope，没有生命周期限制，由它执行的Scope需要自行维护
        // 看一下Job，有没有似曾相似？
        // 一般来说，父协程会等待所有子协程执行完毕才结束
        // 取消了父协程后，子协程会一并被取消掉
        val jobParent: Job = GlobalScope.launch(Dispatchers.Default) {
            val jobChild: Job = launch {
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

    private suspend fun testAsync(view: View) {
        // GlobalScope的async是不会发生异常的，会在await的时候才抛出
        val jobAsync = GlobalScope.async {
            Log.d(LOG_TAG, "async 1")
            throw RuntimeException("fuck")
            Log.d(LOG_TAG, "async 2")
            return@async 1
        }
        var result = try {
            jobAsync.await()
        } catch (e: Exception) {
            Log.d(LOG_TAG, "exception $e")
            0
        }
        Snackbar.make(view, "result is $result", Snackbar.LENGTH_LONG).show()
    }
}