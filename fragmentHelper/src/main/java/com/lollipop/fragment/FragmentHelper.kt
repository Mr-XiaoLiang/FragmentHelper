package com.lollipop.fragment

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

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

        fun bind(viewGroup: ViewPager, stateEnable: Boolean): ViewPagerHelper.V1 {
            return ViewPagerHelper.V1(viewGroup, fragmentManager, stateEnable).apply {
                reset(infoList)
            }
        }

        fun bind(viewGroup: ViewPager2): ViewPagerHelper.V2 {
            return ViewPagerHelper.V2(viewGroup, fragmentManager, lifecycleOwner.lifecycle).apply {
                reset(infoList)
            }
        }

        fun bind(viewGroup: ViewGroup): FragmentSwitcher {
            return FragmentSwitcher(fragmentManager, viewGroup).apply {
                reset(infoList)
            }
        }

    }

}