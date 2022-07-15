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
        private var onFragmentInitCallback: FragmentInitCallback? = null
        private var onFragmentArgumentsChangedCallback: FragmentArgumentsChangedCallback? = null

        fun add(fragment: Class<out Fragment>, pageKey: String): Builder {
            return add(FragmentInfo(fragment, pageKey))
        }

        fun add(info: FragmentInfo): Builder {
            infoList.add(info)
            return this
        }

        fun bind(viewGroup: ViewPager, stateEnable: Boolean = false): ViewPagerHelper.V1 {
            return ViewPagerHelper.V1(viewGroup, fragmentManager, stateEnable).apply {
                initListener(this)
                reset(infoList)
            }
        }

        fun bind(viewGroup: ViewPager2): ViewPagerHelper.V2 {
            return ViewPagerHelper.V2(viewGroup, fragmentManager, lifecycleOwner.lifecycle).apply {
                initListener(this)
                reset(infoList)
            }
        }

        fun bind(viewGroup: ViewGroup): FragmentSwitcher {
            return FragmentSwitcher(fragmentManager, viewGroup).apply {
                initListener(this)
                reset(infoList)
            }
        }

        private fun initListener(controller: FragmentController) {
            onFragmentInitCallback?.let {
                controller.addInitListener(it)
            }
            onFragmentArgumentsChangedCallback?.let {
                controller.addArgumentsChangedListener(it)
            }
        }

        fun onFragmentInit(callback: FragmentInitCallback): Builder {
            this.onFragmentInitCallback = callback
            return this
        }

        fun onArgumentsChanged(callback: FragmentArgumentsChangedCallback): Builder {
            this.onFragmentArgumentsChangedCallback = callback
            return this
        }

    }

}