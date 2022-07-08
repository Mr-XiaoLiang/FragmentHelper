package com.lollipop.fragment

import android.os.Bundle

fun interface FragmentInitCallback {
    fun onFragmentCreated(pageKey: String, arguments: Bundle)
}

fun interface FragmentArgumentsChangedCallback {
    fun onFragmentArgumentsChanged(pageKey: String, arguments: Bundle)
}

interface LollipopPage {
    fun onArgumentsChanged()
}