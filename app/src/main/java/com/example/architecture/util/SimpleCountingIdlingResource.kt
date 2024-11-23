package com.example.architecture.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

/**
 * @项目 ：architecture
 * @包名 ：com.example.architecture.util
 * @作者 : bobo
 * @日期和时间: 2024/11/23 20:24
 **/

/**
 *
 * @描述
 * 287 / 5,000
 * 的简单计数器实现，通过维护内部计数器来确定空闲状态。当计数器为 0 时，它被视为空闲，当计数器为非零时，它不是空闲的。这与 的行为方式非常相似。
 * @作者 bobo
 * @日期及时间 2024/11/23 20:46
 * @日期及时间 2024/11/23 20:46
 */
class SimpleCountingIdlingResource(private val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    /**
      *
      * @描述 增加正在监控的资源的正在进行的交易的数量。
      * @参数 
      * @返回 
      * @作者 bobo
      * @日期及时间 2024/11/23 20:57
      */
    fun increment(){
        counter.getAndIncrement()
    }

    fun decrement(){
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0){
            resourceCallback?.onTransitionToIdle()
        }else if (counterVal < 0){
            throw IllegalStateException("Counter has been corrupted!")
        }
    }

}