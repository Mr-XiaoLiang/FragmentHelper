package com.lollipop.fragmenthelper.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lollipop.fragment.FragmentHelper
import com.lollipop.fragmenthelper.demo.databinding.FragmentViewpagerDemoBinding

class ViewPageDemoFragment : Fragment() {

    private var binding: FragmentViewpagerDemoBinding? = null

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

    }

}