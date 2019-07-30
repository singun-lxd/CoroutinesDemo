package com.singun.coroutines.demo.test

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by singun on 2019-07-29.
 */

private const val LOG_TAG = "coroutines-CoroutinesTest"

object CoroutinesTest {

    suspend fun test(view: View) {
        // 同步执行的请求，先访问google再访问百度
        // 为了大家比较好理解，所以这里用okhttp来讲解，retrofit也有用于协程的adapter
        // 参见https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter
        val result = syncRequest()
        // 异步请求，同时开始google和百度的访问，当两者都完成时返回
        // async/await比较像es标准中的异步操作
//        val result = asyncRequest()
        Snackbar.make(view, "result is $result", Snackbar.LENGTH_LONG).show()
    }

    private suspend fun syncRequest(): String {
        var result: String
        try {
            result = requestGoogle()
            result = requestBaidu()
        } catch (e: Exception) {
            result = "failed: $e"
        }
        return result
    }

    private suspend fun asyncRequest(): String {
        val taskGoogle = GlobalScope.async {
            Log.d(LOG_TAG, "google begin")
            val result = try {
                requestGoogle()
            } catch (e: Exception) {
                "failed: $e"
            }
            Log.d(LOG_TAG,"google finish")
            result
        }
        val taskBaidu = GlobalScope.async {
            Log.d(LOG_TAG, "baidu begin")
            val result = try {
                requestBaidu()
            } catch (e: Exception) {
                "failed: $e"
            }
            Log.d(LOG_TAG, "baidu finish")
            result
        }
        taskGoogle.await()
        return taskBaidu.await()
    }

    private suspend fun requestGoogle() = suspendCoroutine<String> {
        CallbackTest.requestGoogle(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume("request google success")
            }
        })
    }

    private suspend fun requestBaidu() = suspendCoroutine<String> {
        CallbackTest.requestBaidu(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume("success")
            }
        })
    }
}