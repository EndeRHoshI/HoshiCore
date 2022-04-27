package com.hoshi.core.utils

/**
 * Created by lv.qx on 2022/2/11
 */
object EmojiUtils {

    const val ZERO_WIDTH_JOINER = 0x200D // 零宽连接符，十进制 8205
    const val VARIATION_SELECTOR_15 = 0xFE0E // 变量选择器-15，让基础 Emoji 变成更接近文本样式（text-style）
    const val VARIATION_SELECTOR_16 = 0xFE0F // 变量选择器-16，让基础 Emoji 变成更接近 Emoji 样式（emoji-style）
    const val KEY_CAP = 0x20E3 // 键帽序列，配合变量选择器-16，将字符转换为键帽的样式
    val REGIONAL_INDICATOR = 0x1F1E6..0x1F1FF // 地域指示符，共 26 个，每个对应于一个英文字母含义
    val SKIN_TONE = 0x1F3FB..0x1F3FF // 皮肤色调符，由浅到深共 5 个

    /**
     * @param emojiContent 带有 emoji 的文本
     * @return 带有 emoji 文本的真实长度
     */
    fun getLength(emojiContent: CharSequence): Int {
        val length = emojiContent.length // 原始长度
        val codePointCount = emojiContent.toString().codePointCount(0, length) // 码点长度

        // 码点列表
        val codePointList = emojiContent.mapIndexed { index, _ ->
            Character.codePointAt(emojiContent, index)
        }

        // 找出零宽字符的个数
        val zeroWidthCount = codePointList.count { it == ZERO_WIDTH_JOINER }

        // 找出地域指示符的个数
        val regionalCount = codePointList.count { isRegionalIndicator(it) }

        // 找出其他符号的个数
        val otherCount = codePointList.count {
            it == VARIATION_SELECTOR_15
                    || it == VARIATION_SELECTOR_16
                    || it == KEY_CAP
                    || isSkinTone(it)
        }

        var result = codePointCount

        result -= (2 * zeroWidthCount // 去除零宽字符的影响
                + regionalCount / 2 // 去除地域指示符的影响
                + otherCount) // 去除其他指示符的影响

        return result
    }

    /**
     * @param codePoint 码点
     * @return 传入的码点是否是地域指示符
     */
    private fun isRegionalIndicator(codePoint: Int): Boolean = codePoint in REGIONAL_INDICATOR

    /**
     * @param codePoint 码点
     * @return 传入的码点是否是皮肤色调符
     */
    private fun isSkinTone(codePoint: Int): Boolean = codePoint in SKIN_TONE

}