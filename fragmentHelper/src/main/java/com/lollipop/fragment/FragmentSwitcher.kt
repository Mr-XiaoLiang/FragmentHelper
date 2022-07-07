package com.lollipop.fragment

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager

class FragmentSwitcher(
    private val fragmentManager: FragmentManager,
    private val container: ViewGroup
) {

    private val infoMap = HashMap<String, FragmentInfo>()

    private val initCallback = ListenerManager<FragmentInitCallback>()
    private val argumentsChangedCallback = ListenerManager<FragmentArgumentsChangedCallback>()

    var currentPage: String = ""
        private set

    val size: Int
        get() {
            return infoMap.size
        }

    fun add(info: FragmentInfo) {
        infoMap[info.pageKey] = info
        if (info.pageKey == currentPage) {
            switchTo(info.pageKey)
        }
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

    fun addListener(callback: FragmentInitCallback) {
        initCallback.add(callback)
    }

    fun removeListener(callback: FragmentInitCallback) {
        initCallback.remove(callback)
    }

    fun addListener(callback: FragmentArgumentsChangedCallback) {
        argumentsChangedCallback.add(callback)
    }

    fun removeListener(callback: FragmentArgumentsChangedCallback) {
        argumentsChangedCallback.remove(callback)
    }

    private fun onFragmentCreated(pageKey: String, arguments: Bundle) {
        initCallback.invoke { it.onFragmentCreated(pageKey, arguments) }
    }

    private fun onFragmentArgumentsChanged(pageKey: String, arguments: Bundle) {
        argumentsChangedCallback.invoke { it.onFragmentArgumentsChanged(pageKey, arguments) }
    }


    fun switchTo(
        pageKey: String,
        arguments: Bundle? = null
    ) {
        TODO()
    }

}