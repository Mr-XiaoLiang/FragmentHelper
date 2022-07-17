package com.lollipop.fragmenthelper.demo

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lollipop.fragment.FragmentHelper
import com.lollipop.fragment.FragmentInfo
import com.lollipop.fragmenthelper.demo.databinding.FragmentViewpager2DemoBinding


class ViewPage2DemoFragment : Fragment() {

    private var binding: FragmentViewpager2DemoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentViewpager2DemoBinding.inflate(inflater, container, false)
        this.binding = b
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val b = binding ?: return
        val switcher = FragmentHelper.with(this)
            .add(
                FragmentInfo(
                    TempDemoFragment::class.java,
                    "1",
                    titleId = R.string.title1,
                    colorId = R.color.color1
                )
            )
            .add(
                FragmentInfo(
                    TempDemoFragment::class.java,
                    "2",
                    titleId = R.string.title2,
                    colorId = R.color.color2
                )
            )
            .add(
                FragmentInfo(
                    TempDemoFragment::class.java,
                    "3",
                    titleId = R.string.title3,
                    colorId = R.color.color3
                )
            )
            .add(
                FragmentInfo(
                    TempDemoFragment::class.java,
                    "4",
                    titleId = R.string.title4,
                    colorId = R.color.color4
                )
            )
            .bind(b.viewPager)
        switcher.addInitListener { pageKey, arguments ->
            val color = context?.let {
                switcher[pageKey]?.loadColor(it)
            } ?: 0
            TempDemoFragment.bindBundle(arguments, color)
        }

        TabLayoutMediator(
            b.tabLayout, b.viewPager
        ) { tab, position -> //这里可以自定义TabView
            val tabView = TextView(b.viewPager.context)
            val states = arrayOfNulls<IntArray>(2)
            states[0] = intArrayOf(android.R.attr.state_selected)
            states[1] = intArrayOf()
            tabView.text = switcher[position].loadTitle(b.viewPager.resources)
            tabView.gravity = Gravity.CENTER
            tab.customView = tabView
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "共同计数-VP2"
    }

}