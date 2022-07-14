package com.lollipop.fragmenthelper.demo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lollipop.fragment.LollipopPage
import com.lollipop.fragmenthelper.demo.databinding.FragmentTempDemoBinding

class TempDemoFragment : Fragment(), LollipopPage {

    companion object {

        private const val ARG_NUMBER = "ARG_NUMBER"
        private const val ARG_BACKGROUND = "ARG_BACKGROUND"

        fun buildBundle(number: Int, background: Int): Bundle {
            return Bundle().apply {
                putInt(ARG_NUMBER, number)
                putInt(ARG_BACKGROUND, background)
            }
        }
    }

    private var number = 0

    private var binding: FragmentTempDemoBinding? = null

    private var callback: Callback? = null

    override fun onArgumentsChanged() {
        number = arguments?.getInt(ARG_NUMBER, 0) ?: 0
        if (isResumed) {
            updateNumber()
            binding?.valueView?.setBackgroundColor(
                arguments?.getInt(
                    ARG_BACKGROUND,
                    Color.TRANSPARENT
                ) ?: Color.TRANSPARENT
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = findCallback(context)
    }

    private fun findCallback(c: Context): Callback? {
        parentFragment?.let {
            if (it is Callback) {
                return it
            }
        }
        if (c is Callback) {
            return c
        }
        context?.let {
            if (it is Callback) {
                return it
            }
        }
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentTempDemoBinding.inflate(inflater, container, false)
        this.binding = b
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.button?.setOnClickListener {
            number++
            callback?.onNumberChanged(number)
        }
        updateNumber()
    }

    private fun updateNumber() {
        binding?.valueView?.text = number.toString()
    }

    override fun onResume() {
        super.onResume()
        updateNumber()
        binding?.valueView?.setBackgroundColor(
            arguments?.getInt(
                ARG_BACKGROUND,
                Color.TRANSPARENT
            ) ?: Color.TRANSPARENT
        )
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    fun interface Callback {
        fun onNumberChanged(number: Int)
    }

}