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

        private const val ARG_BACKGROUND = "ARG_BACKGROUND"
        private const val ARG_TITLE = "ARG_TITLE"

        fun buildBundle(background: Int): Bundle {
            return Bundle().apply {
                putInt(ARG_BACKGROUND, background)
            }
        }

        fun bindBundle(bundle: Bundle, background: Int): Bundle {
            return bundle.apply {
                putInt(ARG_BACKGROUND, background)
            }
        }

        fun putTitle(bundle: Bundle, title: String) {
            bundle.putString(ARG_TITLE, title)
        }
    }

    private val number: Int
        get() {
            return callback?.getNumber() ?: 0
        }

    private var binding: FragmentTempDemoBinding? = null

    private var callback: Callback? = null

    override fun onArgumentsChanged() {
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
            val n = number + 1
            callback?.onNumberChanged(n)
            updateNumber()
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
        arguments?.getString(ARG_TITLE)?.let {
            activity?.title = it
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    interface Callback {
        fun onNumberChanged(number: Int)
        fun getNumber(): Int
    }

}