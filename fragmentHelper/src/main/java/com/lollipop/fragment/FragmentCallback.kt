package com.lollipop.fragment

import android.os.Bundle

/**
 * Fragment被实例化时的回调函数
 * 主要用于初始化参数的配置
 */
fun interface FragmentInitCallback {
    /**
     * 当Fragment被实例化时触发
     * @param pageKey Fragment的唯一标识
     * @param arguments 参数对象，可以往其中放置参数信息
     */
    fun onFragmentCreated(pageKey: String, arguments: Bundle)
}

/**
 * Fragment参数发生变化时的回调函数
 * 主要用于参数补充和更新
 */
fun interface FragmentArgumentsChangedCallback {
    /**
     * 当Fragment被实例化或者参数变更时触发
     * @param pageKey Fragment的唯一标识
     * @param arguments 参数对象，可以往其中放置参数信息
     */
    fun onFragmentArgumentsChanged(pageKey: String, arguments: Bundle)
}

/**
 * 一个定制的Fragment接口
 * 它将为Fragment提供一个方法，在参数发生变更时触发
 * Fragment可以感知参数发生变化并且主动响应
 */
interface LollipopPage {
    /**
     * Fragment的参数被修改时的回调方法
     */
    fun onArgumentsChanged()
}