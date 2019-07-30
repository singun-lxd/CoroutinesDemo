package com.singun.coroutines.demo.test

import android.os.Looper
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.RuntimeException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * Created by singun on 2019-07-29.
 */
private const val LOG_TAG = "coroutines-ContextTest"

object ContextTest {
    suspend fun test(view: View) {
        testThread(view)
//        testException()
    }

    private suspend fun testThread(view: View) {
        // 上下文用于切换线程非常方便
        // 线程安全：线程用的锁在这里都能用。协程还有专用的锁Mutex。
        var threadName: String = ""
        withContext(Dispatchers.IO) {
            threadName = Thread.currentThread().name
        }
        Snackbar.make(view, "ThreadName: $threadName", Snackbar.LENGTH_LONG).show()

//        val isMainThread = isMainThread()
//        Snackbar.make(view, "CurrentIsMainThread: $isMainThread", Snackbar.LENGTH_LONG).show()
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }

    private fun testException() {
        // 异常处理器对async无效
        // 协程的调度使用了拦截器，如果我们用过OkHttp，都知道拦截器的作用。
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO) + ExceptionHandler()
        scope.launch {
            throw RuntimeException("test")
        }
        scope.launch(Interceptor()) {
            Log.d(LOG_TAG, "for test")
        }
    }

    class ExceptionHandler: AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) {
            Log.d(LOG_TAG, "catch exception $exception")
//            throw exception
        }
    }

    class Interceptor: AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
        override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
            Log.d(LOG_TAG,"Interceptor $continuation" )
            return continuation
        }
    }
}