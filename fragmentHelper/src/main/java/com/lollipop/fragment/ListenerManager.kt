package com.lollipop.fragment

import java.lang.ref.WeakReference

internal class ListenerManager<T> {

    private val list = ArrayList<WeakReference<T>>()

    fun add(listener: T) {
        synchronized(this) {
            list.add(WeakReference(listener))
        }
    }

    fun remove(listener: T) {
        synchronized(this) {
            val temp = HashSet<WeakReference<T>>()
            list.forEach {
                val t = it.get()
                if (t == null || t === listener) {
                    temp.add(it)
                }
            }
            list.removeAll(temp)
        }
    }

    fun invoke(callback: (T) -> Unit) {
        list.forEach {
            it.get()?.let(callback)
        }
    }

}