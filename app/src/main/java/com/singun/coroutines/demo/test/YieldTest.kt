package com.singun.coroutines.demo.test

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.concurrent.Executors

/**
 * Created by singun on 2019/7/28 0028.
 */
object YieldTest {
    suspend fun test(view: View) {
        //协程中带有yield会暂停协程的执行，留给其他协程执行机会
        //如果协程执行耗时操作，在循环中yield可以实现取消功能
        val result = getResult()

        //以下为kotlin标准库实现，不需要协程环境
        //这里的yield与js/C#/python的yield类似，是一个迭代器的语法糖
//        val result = YieldList().joinToString()
//        val result = getFibonacci(10)
        Snackbar.make(view, result, Snackbar.LENGTH_LONG).show()
    }

    private suspend fun getResult(): String {
        val singleDispatcher = Executors.newSingleThreadExecutor{ r -> Thread(r, "MyThread") }.asCoroutineDispatcher()
        val list: MutableList<String> = ArrayList()
        val job = GlobalScope.launch {
            launch(singleDispatcher) {
                repeat(5) {
                    list.add("1")
                    yield()
                }
            }
            launch(singleDispatcher) {
                repeat(5) {
                    list.add("2")
                    yield()
                }
            }
        }
        job.join()
        return list.joinToString()
    }

    private fun getFibonacci(count: Int): String {
        val fibonacciSeq = sequence {
            var a = 0
            var b = 1

            yield(1)

            while (true) {
                yield(a + b)

                val tmp = a + b
                a = b
                b = tmp
            }
        }
        return fibonacciSeq.take(count).joinToString()
    }

    class YieldList : Iterable<Int>{
        private val list = arrayOf(1,2,3,4,5,6)

        override fun iterator(): Iterator<Int> {
            return iterator {
                val size = list.size
                for(i in 0 until size) {
                    yield(list[i] + i)
                }
            }
        }
    }
}