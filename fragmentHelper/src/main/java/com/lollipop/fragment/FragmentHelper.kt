package com.lollipop.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

object FragmentHelper {

    fun with(activity: AppCompatActivity): Builder {
        return Builder(activity.supportFragmentManager, activity)
    }

    fun with(fragment: Fragment): Builder {
        return Builder(fragment.childFragmentManager, fragment)
    }

    class Builder(
        private val fragmentManager: FragmentManager,
        private val lifecycleOwner: LifecycleOwner
    ) {

        private val infoList = ArrayList<FragmentInfo>()

        fun add(fragment: Class<out Fragment>, pageKey: String): Builder {
            return add(FragmentInfo(fragment, pageKey))
        }

        fun add(info: FragmentInfo): Builder {
            infoList.add(info)
            return this
        }

    }

}