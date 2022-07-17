package com.lollipop.fragment

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

object FragmentHelper {

    /**
     * 依赖Activity的FragmentManager创建一个构造工具
     */
    fun with(activity: AppCompatActivity): Builder {
        return Builder(activity.supportFragmentManager, activity)
    }

    /**
     * 依赖Fragment的FragmentManager创建一个构造工具
     */
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

        /**
         * 简单添加一个Fragment信息
         * @param fragment Fragment的类信息，需要提供无参构造器
         * @param pageKey 这个Page的Key，它是唯一的，因为你需要用它来切换页面
         */
        fun add(fragment: Class<out Fragment>, pageKey: String): Builder {
            return add(FragmentInfo(fragment, pageKey))
        }

        /**
         * 添加一个完整的Fragment描述信息
         */
        fun add(info: FragmentInfo): Builder {
            infoList.add(info)
            return this
        }

        /**
         * 绑定到一个ViewPager上
         * @param viewGroup ViewPager对象
         * @param stateEnable true表示使用FragmentStatePagerAdapter，否则使用FragmentPagerAdapter
         */
        fun bind(viewGroup: ViewPager, stateEnable: Boolean = false): ViewPagerHelper.V1 {
            return ViewPagerHelper.V1(viewGroup, fragmentManager, stateEnable).apply {
                initListener(this)
                reset(infoList)
            }
        }

        /**
         * 绑定到一个ViewPager2上
         * @param viewGroup ViewPager2实例
         */
        fun bind(viewGroup: ViewPager2): ViewPagerHelper.V2 {
            return ViewPagerHelper.V2(viewGroup, fragmentManager, lifecycleOwner.lifecycle).apply {
                initListener(this)
                reset(infoList)
            }
        }

        /**
         * 添加到一个普通的ViewGroup上，推荐使用FrameLayout或FragmentContainerView
         * @param viewGroup Fragment容器
         */
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

        /**
         * 添加一个Fragment初始化的监听器
         * 可以通过它在Fragment实例化时添加参数信息
         * @param callback Fragment实例化的回调函数
         */
        fun onFragmentInit(callback: FragmentInitCallback): Builder {
            this.onFragmentInitCallback = callback
            return this
        }

        /**
         * 当Fragment当参数发生变化时的回调函数
         * 它会在FragmentInitCallback之后触发
         * 也可能发生在switchTo时
         * @param callback Fragment参数改变的回调函数
         */
        fun onArgumentsChanged(callback: FragmentArgumentsChangedCallback): Builder {
            this.onFragmentArgumentsChangedCallback = callback
            return this
        }

    }

}