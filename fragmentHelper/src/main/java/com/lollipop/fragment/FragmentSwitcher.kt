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

    var currentPage: String = ""
        private set

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

    fun set(info: FragmentInfo) {
        infoMap[info.pageKey] = info
        if (info.pageKey == currentPage) {
            switchTo(info.pageKey)
        }
    }

    fun findInfo(key: String): FragmentInfo? {
        if (infoMap.isEmpty()) {
            return null
        }
        return infoMap[key]
    }

    fun remove(pageKey: String, backup: String = "") {
        infoMap.remove(pageKey)
        if (pageKey == currentPage) {
            switchTo(backup)
        }
    }

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

    fun popBackStack(name: String = "") {
        if (name.isEmpty()) {
            fragmentManager.popBackStack()
        } else {
            fragmentManager.popBackStack(name, 0)
        }
    }

    fun findFragment(key: String): Fragment? {
        return findFragmentByKey(fragmentManager, container, key)
    }

    inline fun <reified T : Fragment> findTypedFragment(key: String): T? {
        val fragment = findFragment(key)
        if (fragment is T) {
            return fragment
        }
        return null
    }

}