package com.lollipop.fragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

sealed class ViewPagerHelper : FragmentController() {

    companion object {
        private const val ID_MASK = 0xFFFFFFFFL
    }

    private val fragmentList = ArrayList<FragmentInfo>()

    protected abstract fun notifyDataSetChanged()
    protected abstract fun notifyItemChanged(index: Int, key: String)
    protected abstract fun notifyItemInserted(index: Int, key: String)
    protected abstract fun notifyItemRemoved(index: Int, key: String)

    abstract val currentItem: Int

    fun switch(pageKey: String) {
        for (i in fragmentList.indices) {
            if (fragmentList[i].pageKey == pageKey) {
                switch(i)
                break
            }
        }
    }

    abstract fun switch(index: Int)

    override val size: Int
        get() {
            return fragmentList.size
        }

    operator fun get(index: Int): FragmentInfo {
        return fragmentList[index]
    }

    protected fun loadTitle(context: Context, index: Int): String {
        if (index in 0 until size) {
            return loadTitle(fragmentList[index], context)
        }
        return ""
    }

    protected fun createFragment(index: Int): Fragment {
        return createFragment(fragmentList[index], null)
    }

    fun reset(list: List<FragmentInfo>) {
        fragmentList.clear()
        fragmentList.addAll(list)
        notifyDataSetChanged()
    }

    fun add(info: FragmentInfo) {
        val start = fragmentList.size
        fragmentList.add(info)
        notifyItemInserted(start, info.pageKey)
    }

    fun remove(info: FragmentInfo) {
        val index = fragmentList.indexOf(info)
        if (index >= 0) {
            fragmentList.removeAt(index)
            notifyItemRemoved(index, info.pageKey)
        }
    }

    fun set(index: Int, info: FragmentInfo) {
        fragmentList[index] = info
        notifyItemChanged(index, info.pageKey)
    }

    protected fun getItemId(position: Int): Long? {
        if (position < 0 || position >= size) {
            return null
        }
        val info = get(position)
        val hashCode = System.identityHashCode(info).toLong()
        val positionLong = position.toLong()
        return hashCode shl 32 or positionLong
    }

    protected fun containsItem(itemId: Long): Boolean {
        val position = (itemId and ID_MASK).toInt()
        if (position < 0 || position >= size) {
            return false
        }
        val hashCode = (itemId shr 32 and ID_MASK).toInt()
        val info = get(position)
        return System.identityHashCode(info) == hashCode
    }

    class V1(
        private val viewPager: ViewPager,
        fragmentManager: FragmentManager,
        stateEnable: Boolean = true
    ) : ViewPagerHelper() {

        init {
            viewPager.adapter = if (stateEnable) {
                StateAdapter(fragmentManager, ::size, ::createFragment, ::loadTitleByIndex)
            } else {
                OrdinaryAdapter(
                    fragmentManager,
                    ::size,
                    ::getItemId,
                    ::createFragment,
                    ::loadTitleByIndex
                )
            }
        }

        private fun loadTitleByIndex(index: Int): String {
            return loadTitle(viewPager.context, index)
        }

        override fun notifyDataSetChanged() {
            viewPager.adapter?.notifyDataSetChanged()
        }

        override fun notifyItemChanged(index: Int, key: String) {
            notifyDataSetChanged()
        }

        override fun notifyItemInserted(index: Int, key: String) {
            notifyDataSetChanged()
        }

        override fun notifyItemRemoved(index: Int, key: String) {
            notifyDataSetChanged()
        }

        override val currentItem: Int
            get() {
                return viewPager.currentItem
            }

        override fun switch(index: Int) {
            viewPager.currentItem = index
        }

        private class StateAdapter(
            manager: FragmentManager,
            private val sizeProvider: () -> Int,
            private val infoProvider: (position: Int) -> Fragment,
            private val titleProvider: (position: Int) -> String
        ) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            override fun getCount(): Int {
                return sizeProvider()
            }

            override fun getItem(position: Int): Fragment {
                return infoProvider(position)
            }

            override fun getPageTitle(position: Int): CharSequence {
                return titleProvider(position)
            }

        }

        private class OrdinaryAdapter(
            manager: FragmentManager,
            private val sizeProvider: () -> Int,
            private val itemIdProvider: (position: Int) -> Long?,
            private val infoProvider: (position: Int) -> Fragment,
            private val titleProvider: (position: Int) -> CharSequence
        ) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            override fun getCount(): Int {
                return sizeProvider()
            }

            override fun getItem(position: Int): Fragment {
                return infoProvider(position)
            }

            override fun getPageTitle(position: Int): CharSequence {
                return titleProvider(position)
            }

            override fun getItemId(position: Int): Long {
                return itemIdProvider(position) ?: position.toLong()
            }

        }

    }

    class V2(
        private val viewPager: ViewPager2,
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) : ViewPagerHelper() {

        init {
            viewPager.adapter = Adapter(
                fragmentManager,
                lifecycle,
                ::size,
                ::getItemId,
                ::containsItem,
                ::createFragment
            )
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun notifyDataSetChanged() {
            viewPager.adapter?.notifyDataSetChanged()
        }

        override fun notifyItemChanged(index: Int, key: String) {
            viewPager.adapter?.notifyItemChanged(index)
        }

        override fun notifyItemInserted(index: Int, key: String) {
            viewPager.adapter?.notifyItemInserted(index)
        }

        override fun notifyItemRemoved(index: Int, key: String) {
            viewPager.adapter?.notifyItemRemoved(index)
        }

        override val currentItem: Int
            get() {
                return viewPager.currentItem
            }

        override fun switch(index: Int) {
            viewPager.currentItem = index
        }

        private class Adapter(
            fragmentManager: FragmentManager,
            lifecycle: Lifecycle,
            private val sizeProvider: () -> Int,
            private val itemIdProvider: (position: Int) -> Long?,
            private val containsItemCallback: (itemId: Long) -> Boolean,
            private val pageProvider: (position: Int) -> Fragment,
        ) : FragmentStateAdapter(fragmentManager, lifecycle) {

            override fun getItemCount(): Int {
                return sizeProvider()
            }

            override fun createFragment(position: Int): Fragment {
                return pageProvider(position)
            }

            override fun getItemId(position: Int): Long {
                return itemIdProvider(position) ?: RecyclerView.NO_ID
            }

            override fun containsItem(itemId: Long): Boolean {
                return containsItemCallback(itemId)
            }

        }

    }

}