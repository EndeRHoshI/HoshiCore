package com.hoshi.core.router

import com.hoshi.core.AppState

/**
 * 路由抽象类，继承此类，然后使用 bindRootAction 方法绑定 Action 名
 */
abstract class RouterActions {

    /**
     * 初始化包名前缀
     */
    private fun initPackagePrefix(): String {
        return AppState.getPackageName()
    }

    /**
     * 绑定 Action 名
     */
    fun bindRootAction(name: String): String {
        return initPackagePrefix() + "." + name
    }

}