package com.singun.coroutines.demo

import android.view.View
import com.singun.coroutines.demo.test.*

/**
 * Created by singun on 2019/7/28 0028.
 */
class Test {
    suspend fun test(view: View) {
        CallbackTest.test(view)
//        ScopeTest.test(view)
//        ContextTest.test(view)
//        YieldTest.test(view)
//        CoroutinesTest.test(view)
//        TimeoutTest.test(view)
    }
}