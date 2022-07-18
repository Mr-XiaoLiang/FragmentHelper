package com.lollipop.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class FragmentController {

    private val initCallback = ListenerManager<FragmentInitCallback>()
    private val argumentsChangedCallback = ListenerManager<FragmentArgumentsChangedCallback>()

    /**
     * 添加初始化的监听器
     */
    fun addInitListener(callback: FragmentInitCallback) {
        initCallback.add(callback)
    }

    /**
     * 移除初始化监听器
     */
    fun removeListener(callback: FragmentInitCallback) {
        initCallback.remove(callback)
    }

    /**
     * 添加参数变更的监听器
     */
    fun addArgumentsChangedListener(callback: FragmentArgumentsChangedCallback) {
        argumentsChangedCallback.add(callback)
    }

    /**
     * 移除参数变更的监听器
     */
    fun removeListener(callback: FragmentArgumentsChangedCallback) {
        argumentsChangedCallback.remove(callback)
    }

    protected fun onFragmentCreated(pageKey: String, arguments: Bundle) {
        initCallback.invoke { it.onFragmentCreated(pageKey, arguments) }
    }

    protected fun onFragmentArgumentsChanged(pageKey: String, arguments: Bundle) {
        argumentsChangedCallback.invoke { it.onFragmentArgumentsChanged(pageKey, arguments) }
    }

    /**
     * page的数量
     * 它表示FragmentInfo的数量，并不表示已经被实例化的Fragment的数量
     */
    abstract val size: Int

    private fun onArgumentsChanged(fragment: Fragment) {
        if (fragment is LollipopPage) {
            fragment.onArgumentsChanged()
        }
    }

    protected fun createFragment(fragmentInfo: FragmentInfo, bundle: Bundle?): Fragment {
        val fragment = fragmentInfo.fragment.newInstance()
        val arguments = fragment.arguments ?: Bundle()
        if (bundle != null) {
            arguments.putAll(bundle)
        }
        onFragmentCreated(fragmentInfo.pageKey, arguments)
        onFragmentArgumentsChanged(fragmentInfo.pageKey, arguments)
        fragment.arguments = arguments
        onArgumentsChanged(fragment)
        return fragment
    }

    protected fun loadTitle(info: FragmentInfo, context: Context): String {
        return context.getString(info.titleId)
    }

}