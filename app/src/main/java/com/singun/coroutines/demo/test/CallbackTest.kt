package com.singun.coroutines.demo.test

import android.view.View
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import java.io.IOException

/**
 * Created by singun on 2019/7/28 0028.
 */
object CallbackTest {
    fun test(view: View) {
        // 回调很多时候看起来会更像在写bug
        // okhttp还帮我们封装了线程切换的逻辑，如果自己写handler切换则复杂度增加
        requestGoogle(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                val result = "request google fail: $e"
                showUI(view, result)
            }

            override fun onResponse(call: Call, response: Response) {
                requestBaidu(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        val result = "request baidu fail: $e"
                        showUI(view, result)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val result = "success"
                        showUI(view, result)
                    }
                })
            }
        })
    }

    private fun showUI(view: View, result: String) {
        Snackbar.make(view, "result is $result", Snackbar.LENGTH_LONG).show()
    }

    fun requestGoogle(callback: Callback) {
        val call = OkHttpClient().newCall(
            Request.Builder()
                .get().url("https://www.google.com.hk")
                .build())
        call.enqueue(callback)
    }

    fun requestBaidu(callback: Callback) {
        val call = OkHttpClient().newCall(
            Request.Builder()
                .get().url("https://www.baidu.com")
                .build())
        call.enqueue(callback)
    }
}