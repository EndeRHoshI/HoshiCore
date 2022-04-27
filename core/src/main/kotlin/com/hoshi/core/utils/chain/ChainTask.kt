package com.hoshi.core.utils.chain

import androidx.annotation.CallSuper
import com.hoshi.core.extentions.matchTrue
import com.hoshi.core.utils.HLog
import java.util.concurrent.CountDownLatch

/**
 * 链式任务
 *
 * 使用方法：
 * 1. 实现该类，重写 execute() 方法执行同步或异步方法
 * 2. 执行完需要的操作后，在对应成功或失败的地方调用 onTaskSuccess() 和 onTaskFailure() 方法
 * 3. 将实现类传入 TaskChain 任务链中执行
 */
abstract class ChainTask {

    companion object {
        private const val TAG = "ChainTask"
    }

    private val countDownLatch = CountDownLatch(1) // CountDownLatch 实现异步转同步
    private var isInterrupt = false // 中断标志位，注意设置后再调用 countDown
    private var nextTask: ChainTask? = null // 下一个任务

    /**
     * 设置下一个任务
     * @param nextTask 下一个任务
     */
    fun setNextTask(nextTask: ChainTask?) {
        this.nextTask = nextTask
    }

    /**
     * 开始执行一系列的操作
     */
    fun action() {
        preExecute() // 执行前操作
        execute() // 执行操作
        try {
            countDownLatch.await() // await 阻塞住，等待 execute 中 countDown
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // 如果下面还有任务，且没检测到中断，继续往下走，否则任务链结束
        if (nextTask != null && !isInterrupt) {
            nextTask?.action()
        }
    }

    /**
     * 执行前操作，如果需要的话可以重写该方法
     */
    fun preExecute() {}

    /**
     * 执行操作，实现该抽象方法以完成任务，这个方法可能在主线程也可能在子线程，看具体的 TaskChain 调用第一个 task 的 action 方法在哪里
     */
    abstract fun execute()

    /**
     * 任务执行成功，走下一个任务，execute 中至少要调用成功或失败中的任一个方法
     */
    @CallSuper
    open fun onTaskSuccess() {
        onTaskSuccess(false) // 成功默认不中断，继续往下走
    }

    /**
     * 任务执行成功，根据 isInterrupt 决定是否继续走下一个任务，execute 中至少要调用成功或失败中的任一个方法
     *
     * @param isInterrupt 是否中断（true -> 任务走到这里就可以了，不往下走了；false -> 成功了但是继续往下走）
     */
    @CallSuper
    open fun onTaskSuccess(isInterrupt: Boolean) {
        this.isInterrupt = isInterrupt // 成功了，设置中断，然后才 countDown
        countDownLatch.countDown()
        HLog.d(TAG, "任务成功，" + isInterrupt.matchTrue("不再往下走了", "继续往下走"))
        if (nextTask == null) {
            // 后面没有任务了，可以视为全部任务成功了，取消 loading
            HLog.d(TAG, "任务全部成功")
        }
    }

    /**
     * 任务执行失败，execute 中至少要调用成功或失败中的任一个方法
     */
    @CallSuper
    open fun onTaskFailure() {
        onTaskFailure(true) // 错误默认中断，不继续往下走了
    }

    /**
     * 任务执行失败，execute 中至少要调用成功或失败中的任一个方法
     *
     * @param isInterrupt 是否中断（true -> 任务走到这里就可以了，不往下走了；false -> 失败了但是继续往下走）
     */
    @CallSuper
    open fun onTaskFailure(isInterrupt: Boolean) {
        this.isInterrupt = isInterrupt // 错误了，设置中断，然后才 countDown
        countDownLatch.countDown()
        // 任务失败，取消 loading
        HLog.d(TAG, "任务失败，" + isInterrupt.matchTrue("不再往下走了", "继续往下走"))
    }
}