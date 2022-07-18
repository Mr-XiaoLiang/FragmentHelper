package com.lollipop.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

class FragmentSwitcher(
    private val fragmentManager: FragmentManager,
    private val container: ViewGroup
) : FragmentController() {

    companion object {
        fun getTag(container: ViewGroup, key: String): String {
            val id = if (container.id == 0 || container.id == View.NO_ID) {
                System.identityHashCode(container)
            } else {
                container.id
            }
            return "Lollipop.Switcher:$id:$key"
        }

        fun findFragment(manager: FragmentManager, tag: String): Fragment? {
            return manager.findFragmentByTag(tag)
        }

        fun findFragmentByKey(
            manager: FragmentManager,
            container: ViewGroup,
            key: String
        ): Fragment? {
            return findFragment(manager, getTag(container, key))
        }
    }

    private val infoMap = HashMap<String, FragmentInfo>()

    /**
     * 获取当前选中的Page的key
     */
    var currentPage: String = ""
        private set

    /**
     * 是否开启返回栈
     */
    var isBackstackEnable = false

    init {
        val containerId = container.id
        if (containerId == 0 || containerId == View.NO_ID) {
            container.id = View.generateViewId()
        }
    }

    override val size: Int
        get() {
            return infoMap.size
        }

    /**
     * 设置一个FragmentInfo
     * 如果存在同名的Fragment，那么将会被覆盖
     * 如果当前已经选中了这个Fragment，那么将会更新或者切换
     */
    fun set(info: FragmentInfo) {
        infoMap[info.pageKey] = info
        if (info.pageKey == currentPage) {
            switchTo(info.pageKey)
        }
    }

    /**
     * 按照Key来寻找一个FragmentInfo
     */
    fun findInfo(key: String): FragmentInfo? {
        if (infoMap.isEmpty()) {
            return null
        }
        return infoMap[key]
    }

    /**
     * 移除一个指定Key的FragmentInfo
     * @param backup 表示如果当前选中的是被移除的Fragment，那么以backup为替换
     */
    fun remove(pageKey: String, backup: String = "") {
        infoMap.remove(pageKey)
        if (pageKey == currentPage) {
            switchTo(backup)
        }
    }

    /**
     * 重置所有的FragmentInfo
     * @param backup 表示被重置之后选中的Fragment
     */
    fun reset(list: List<FragmentInfo>, backup: String = "") {
        infoMap.clear()
        list.forEach {
            infoMap[it.pageKey] = it
        }
        switchTo(backup)
    }

    private fun onArgumentsChanged(fragment: Fragment) {
        if (fragment is LollipopPage) {
            fragment.onArgumentsChanged()
        }
    }

    /**
     * 切换为新的Fragment
     * @param pageKey 新的Fragment的pageKey
     * @param arguments 仅本次有效的参数信息，它与初始化参数并不冲突
     */
    fun switchTo(
        pageKey: String,
        arguments: Bundle? = null
    ): String {
        val transaction = fragmentManager.beginTransaction()
        findFragment(currentPage)?.let {
            transaction.hide(it)
            transaction.setMaxLifecycle(it, Lifecycle.State.STARTED)
        }
        val fragmentInfo = findInfo(pageKey)
        if (fragmentInfo != null) {
            var fragment = findFragment(pageKey)
            if (fragment != null && fragment::class.java != fragmentInfo.fragment) {
                transaction.remove(fragment)
                fragment = null
            }
            if (fragment == null) {
                val newFragment = createFragment(fragmentInfo, arguments)
                transaction.add(container.id, newFragment, getTag(container, pageKey))
                transaction.setMaxLifecycle(newFragment, Lifecycle.State.RESUMED)
            } else {
                transaction.show(fragment)
                transaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                updateFragment(fragment, pageKey, arguments)
            }
        }
        val backStack: String
        if (isBackstackEnable) {
            backStack = "${currentPage}->${pageKey}"
            transaction.addToBackStack(backStack)
        } else {
            backStack = ""
        }
        transaction.commit()
        currentPage = pageKey
        return backStack
    }

    private fun updateFragment(fragment: Fragment, pageKey: String, bundle: Bundle?) {
        val arguments = fragment.arguments ?: Bundle()
        if (bundle != null) {
            arguments.putAll(bundle)
        }
        onFragmentArgumentsChanged(pageKey, arguments)
        fragment.arguments = arguments
        onArgumentsChanged(fragment)
    }

    /**
     * 弹出返回栈
     * 如果返回栈是关闭的，那么可能无法正确的完成弹出返回栈
     * @param name 返回栈的名称，如果为空，那么将会直接弹出最上层的栈，否则将会弹出到指定的栈
     */
    fun popBackStack(name: String = "") {
        if (name.isEmpty()) {
            fragmentManager.popBackStack()
        } else {
            fragmentManager.popBackStack(name, 0)
        }
    }

    /**
     * 按照pageKey查找Fragment
     * 如果一个Fragment没有被实例化，那么将会找不到
     * @param key FragmentInfo中的pageKey
     */
    fun findFragment(key: String): Fragment? {
        return findFragmentByKey(fragmentManager, container, key)
    }

    /**
     * 按照pageKey查找Fragment
     * 如果一个Fragment没有被实例化，那么将会找不到
     * 如果查找到的Fragment类型不符，也会返回null
     */
    inline fun <reified T> findTypedFragment(key: String): T? {
        val fragment = findFragment(key)
        if (fragment is T) {
            return fragment
        }
        return null
    }

}