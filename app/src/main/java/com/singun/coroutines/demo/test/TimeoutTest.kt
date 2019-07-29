package com.singun.coroutines.demo.test

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.resumeWithException

/**
 * Created by singun on 2019-07-29.
 */
object TimeoutTest {
    suspend fun test(view: View) {
        var result: String?
        try {
            result = requestGoogle()
//            result = requestGoogleWithTimeout()
        } catch (e: Exception) {
            result = "failed: $e"
        }
        Snackbar.make(view, "result is $result", Snackbar.LENGTH_LONG).show()
    }

    private suspend fun requestGoogleWithTimeout(): String? {
        return withTimeout(5) {
            requestGoogle()
        }
    }

    private suspend fun requestGoogle() = suspendCancellableCoroutine<String> {
        val call = OkHttpClient().newCall(
            Request.Builder()
                .get().url("https://www.google.com.hk")
                .build())

        it.invokeOnCancellation {
            call.cancel()
        }

        call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume("request google success") {
                    // close resource
                }
            }
        })
    }
}