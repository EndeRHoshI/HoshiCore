package com.hoshi.core.utils

/**
 * 退出检查管理器
 *
 * @author lv.qx on 2019/12/20
 */
class ExitCheckManager {

    private val handler = AsyncLooper.handler
    private val exitResetRunnable = ExitResetRunnable()
    private var exitAction: (() -> Unit)? = null
    private var exitConfirm: Boolean = false

    /**
     * 设置退出回调
     *
     * @param exitAction 退出回调
     */
    fun setOnExitAction(exitAction: () -> Unit) {
        this.exitAction = exitAction
    }

    /**
     * 检查退出状态
     */
    fun checkExit(exitConfirmAction: () -> Unit) {
        if (exitConfirm) {
            handler.removeCallbacks(exitResetRunnable)
            postExit()
        } else {
            exitConfirm = true
            exitConfirmAction.invoke()
            handler.postDelayed(exitResetRunnable, EXIT_UNCHECK_DURATION)
        }
    }

    private fun postExit() {
        exitAction?.invoke()
    }

    private inner class ExitResetRunnable : Runnable {
        override fun run() {
            handler.removeCallbacks(this)
            exitConfirm = false
        }
    }

    companion object {
        private const val EXIT_UNCHECK_DURATION = 2000L
    }

}
