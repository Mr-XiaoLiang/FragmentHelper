package com.lollipop.fragmenthelper.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lollipop.fragment.FragmentHelper
import com.lollipop.fragmenthelper.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TempDemoFragment.Callback {

    companion object {
        private const val TAG_VIEWPAGER = "TAG_VIEWPAGER"
        private const val TAG_VIEWPAGER2 = "TAG_VIEWPAGER2"
        private const val TAG_TEMP = "TAG_TEMP"
        private const val TAG_TEMP2 = "TAG_TEMP2"
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val switcher = FragmentHelper.with(this)
            .add(ViewPageDemoFragment::class.java, TAG_VIEWPAGER)
            .add(ViewPage2DemoFragment::class.java, TAG_VIEWPAGER2)
            .add(TempDemoFragment::class.java, TAG_TEMP)
            .add(TempDemoFragment::class.java, TAG_TEMP2)
            .bind(binding.fragmentContainerView)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.viewPager -> {
                    switcher.switchTo(TAG_VIEWPAGER)
                }
                R.id.viewPager2 -> {
                    switcher.switchTo(TAG_VIEWPAGER2)
                }
                R.id.temp1 -> {
                    switcher.switchTo(
                        TAG_TEMP,
                        TempDemoFragment.buildBundle(
                            ContextCompat.getColor(this, R.color.purple_200)
                        ).apply {
                            TempDemoFragment.putTitle(this, "共同计数-T1")
                        }
                    )
                }
                R.id.temp2 -> {
                    switcher.switchTo(
                        TAG_TEMP2,
                        TempDemoFragment.buildBundle(
                            ContextCompat.getColor(this, R.color.purple_500)
                        ).apply {
                            TempDemoFragment.putTitle(this, "共同计数-T2")
                        }
                    )
                }
            }
            true
        }
        switcher.switchTo(TAG_VIEWPAGER)
    }

    override fun onNumberChanged(number: Int) {
        this.number = number
    }

    override fun getNumber(): Int {
        return number
    }

}