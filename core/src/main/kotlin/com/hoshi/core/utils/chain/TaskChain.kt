package com.hoshi.core.utils.chain

/**
 * 任务链
 *
 * 使用方法：
 * 1. 实现该类，重写 genList() 方法依次传入需要顺序执行的链式任务
 * 2. 创建实现类实例，调用 action() 方法开始执行链式任务
 */
abstract class TaskChain {

    /**
     * @return 生成要执行的任务列表
     */
    abstract fun genList(): List<ChainTask>

    /**
     * 开始执行，执行列表里的第一个任务
     */
    fun action() {
        // action 前，对任务链进行初始化
        val tempList = genList()
        if (tempList.isEmpty()) return
        val listSize = tempList.size
        for (i in 0 until listSize) {
            val task = tempList[i]
            if (i < listSize - 1) {
                task.setNextTask(tempList[i + 1]) // 设置下一个 task
            }
        }
        tempList.first().action()
    }

}