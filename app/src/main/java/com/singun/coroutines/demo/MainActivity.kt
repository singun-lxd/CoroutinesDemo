package com.singun.coroutines.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

/**
 * 利用了Kotlin的委托语法，MainActivity继承了CoroutineScope的接口
 * 有关livedata的用法可以参见https://developer.android.com/topic/libraries/architecture/coroutines
 */
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() + CoroutineName("MainActivity") {
    private val test = Test()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            launch {
                test.test(view)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
