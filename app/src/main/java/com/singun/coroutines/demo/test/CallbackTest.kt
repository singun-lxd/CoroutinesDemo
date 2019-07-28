package com.singun.coroutines.demo.test

import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by singun on 2019/7/28 0028.
 */
object CallbackTest {
    fun test(view: View) {
        val handler = Handler(Looper.getMainLooper())
        runBackgroundTask(object: Callback {
            override fun onSuccess() {
                handler.post {
                    showUI(view, "success")
                }
            }

            override fun onFail(msg: String) {
                handler.post {
                    showUI(view, "failed: $msg")
                }
            }
        })
    }

    private fun showUI(view: View, result: String) {
        Snackbar.make(view, "result is $result", Snackbar.LENGTH_LONG).show()
    }

    fun runBackgroundTask(callback: Callback) {
        val thread = object: Thread() {
            override fun run() {
                try {
                    sleep(5000)
                    callback.onSuccess()
                } catch (e: InterruptedException) {
                    callback.onFail("cancelled")
                }
            }
        }
        thread.start()
    }

    interface Callback {
        fun onSuccess()
        fun onFail(msg: String)
    }
}