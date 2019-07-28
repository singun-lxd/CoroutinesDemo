package com.singun.coroutines.demo

import android.view.View
import com.singun.coroutines.demo.test.CallbackTest
import com.singun.coroutines.demo.test.DispatcherTest
import com.singun.coroutines.demo.test.YieldTest

/**
 * Created by singun on 2019/7/28 0028.
 */
class Test {
    suspend fun test(view: View) {
        CallbackTest.test(view)
//        DispatcherTest.test(view)
//        YieldTest.test(view)
    }
}