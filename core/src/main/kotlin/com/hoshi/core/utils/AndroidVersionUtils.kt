package com.hoshi.core.utils

import android.os.Build
import androidx.annotation.IntDef

/**
 *
 *
 * @author lv.qx at 2021/03/19
 */
object AndroidVersionUtils {

    fun isLessThan(@AndroidVersion androidVersion: Int): Boolean {
        return Build.VERSION.SDK_INT < androidVersion
    }

    fun <T> lessThanMatch(@AndroidVersion androidVersion: Int, matchTrue: T, matchFalse: T): T {
        return if (isLessThan(androidVersion)) {
            matchTrue
        } else {
            matchFalse
        }
    }

    /**
     * 通过注解限定类型
     */
    @IntDef(
        Build.VERSION_CODES.BASE,
        Build.VERSION_CODES.BASE_1_1,
        Build.VERSION_CODES.CUPCAKE,
        Build.VERSION_CODES.DONUT,
        Build.VERSION_CODES.ECLAIR,
        Build.VERSION_CODES.ECLAIR_0_1,
        Build.VERSION_CODES.ECLAIR_MR1,
        Build.VERSION_CODES.FROYO,
        Build.VERSION_CODES.GINGERBREAD,
        Build.VERSION_CODES.GINGERBREAD_MR1,
        Build.VERSION_CODES.HONEYCOMB,
        Build.VERSION_CODES.HONEYCOMB_MR1,
        Build.VERSION_CODES.HONEYCOMB_MR2,
        Build.VERSION_CODES.ICE_CREAM_SANDWICH,
        Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1,
        Build.VERSION_CODES.JELLY_BEAN,
        Build.VERSION_CODES.JELLY_BEAN_MR1,
        Build.VERSION_CODES.JELLY_BEAN_MR2,
        Build.VERSION_CODES.KITKAT,
        Build.VERSION_CODES.KITKAT_WATCH,
        Build.VERSION_CODES.LOLLIPOP,
        Build.VERSION_CODES.LOLLIPOP_MR1,
        Build.VERSION_CODES.M,
        Build.VERSION_CODES.N,
        Build.VERSION_CODES.N_MR1,
        Build.VERSION_CODES.O,
        Build.VERSION_CODES.O_MR1,
        Build.VERSION_CODES.P,
        Build.VERSION_CODES.Q,
        Build.VERSION_CODES.R
    )
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class AndroidVersion
}