package com.glimmer.dsl.adapter.vh

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

class ViewHolderCreatorDsl<BEAN : Any, VB : ViewDataBinding>(@LayoutRes private val layoutRes: Int) {
    internal var isViewType: ((Any?) -> Boolean) = { true }
    internal var spanSize: (() -> Int)? = null
    private var setUpData: (CommonVH<BEAN, VB>.() -> Unit)? = null

    fun isViewType(viewType: (Any?) -> Boolean) {
        isViewType = viewType
    }

    fun spanSizeUp(spanSize: (() -> Int)) {
        this.spanSize = spanSize
    }

    fun createVH(setUpData: CommonVH<BEAN, VB>.() -> Unit) {
        this.setUpData = setUpData
    }

    internal fun createVH(parent: ViewGroup): CommonVH<BEAN, VB> {
        val vh = CommonVH<BEAN, VB>(parent, layoutRes)
        setUpData?.let { vh.also(it) }
        return vh
    }

}