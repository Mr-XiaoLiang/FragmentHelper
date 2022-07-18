package com.lollipop.fragment

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class FragmentInfo(
    /**
     * Fragment的实现类
     * 不需要创建实例
     * 实例的管理由FragmentManager进行调度
     * 但是Fragment
     */
    val fragment: Class<out Fragment>,

    /**
     * fragment的唯一标识符
     * Fragment的Class可能是重复的，但是pageKey则可以作为区分
     */
    val pageKey: String,

    /**
     * Fragment标题对应的ID
     * 它默认为0，因为一般情况下可能不需要
     */
    @StringRes
    val titleId: Int = 0,

    /**
     * Fragment对应的颜色的ID
     * 它默认为0，因为一般情况下不需要
     */
    @ColorRes
    val colorId: Int = 0,

    /**
     * Fragment对应的Icon的ID
     * 它默认为0，因为一般情况下不需要
     */
    @DrawableRes
    val iconId: Int = 0
) {

    /**
     * 加载标题信息
     */
    fun loadTitle(resources: Resources): CharSequence {
        if (titleId == 0) {
            return ""
        }
        return resources.getString(titleId)
    }

    /**
     * 加载icon信息
     */
    fun loadIcon(context: Context): Drawable? {
        if (iconId == 0) {
            return null
        }
        return ContextCompat.getDrawable(context, iconId)
    }

    /**
     * 加载颜色信息
     */
    fun loadColor(context: Context): Int {
        if (colorId == 0) {
            return 0
        }
        return ContextCompat.getColor(context, colorId)
    }

}