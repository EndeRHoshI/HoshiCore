package com.hoshi.core.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.hoshi.core.extentions.whenTrue

object ForeBackUtils {

    private var activityNum = 0

    /**
     * 前后台监听
     * 注意：如果横竖屏切换时，活动重建，可能会触发切换到后台监听，需要留心，可能的解决方法有：
     * 1. 阻止活动重建
     * 2. 另行处理？
     *
     * @param application application，前后台监听理应是整个应用注册的监听，所以暂时不做活动的监听，且 application 监听不需要反注册
     * @param switchToForeAction 切换到前台 action
     * @param switchToBackAction 切换到后台 action
     */
    fun registerForeBackListener(
        application: Application,
        switchToForeAction: () -> Unit,
        switchToBackAction: () -> Unit
    ) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                (activityNum == 0).whenTrue {
                    switchToForeAction.invoke()
                    HLog.d("切换到前台")
                }
                activityNum++
                HLog.d("当前活动数 = $activityNum")
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                activityNum--
                HLog.d("当前活动数 = $activityNum")
                (activityNum == 0).whenTrue {
                    switchToBackAction.invoke()
                    HLog.d("切换到后台")
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}