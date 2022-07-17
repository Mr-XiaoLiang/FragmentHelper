package com.lollipop.fragmenthelper.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lollipop.fragment.FragmentHelper
import com.lollipop.fragment.FragmentInfo
import com.lollipop.fragmenthelper.demo.databinding.FragmentViewpagerDemoBinding

class ViewPageDemoFragment : Fragment(), TempDemoFragment.Callback {

    private var binding: FragmentViewpagerDemoBinding? = null

    private var number = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentViewpagerDemoBinding.inflate(inflater, container, false)
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
        b.tabLayout.setupWithViewPager(b.viewPager)
    }

    override fun onNumberChanged(number: Int) {
        this.number = number
    }

    override fun getNumber(): Int {
        return number
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "独立计数"
    }

}