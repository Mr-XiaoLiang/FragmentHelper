package com.lollipop.fragmenthelper.demo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.lollipop.fragment.FragmentHelper
import com.lollipop.fragment.FragmentInfo
import com.lollipop.fragmenthelper.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_VIEWPAGER = "TAG_VIEWPAGER"
        private const val TAG_VIEWPAGER2 = "TAG_VIEWPAGER2"
        private const val TAG_TEMP = "TAG_TEMP"
        private const val TAG_TEMP2 = "TAG_TEMP2"
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FragmentHelper.with(this)
            .add(ViewPageDemoFragment::class.java, TAG_VIEWPAGER)
            .add(ViewPage2DemoFragment::class.java, TAG_VIEWPAGER2)
            .add(FragmentInfo(TempDemoFragment::class.java, TAG_TEMP, colorId = R.color.purple_200))
            .add(FragmentInfo(TempDemoFragment::class.java, TAG_TEMP2, colorId = R.color.purple_500))
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.viewPager -> {

                }
                R.id.viewPager2 -> {

                }
                R.id.temp1 -> {

                }
                R.id.temp2 -> {

                }
            }
            true
        }
    }

}