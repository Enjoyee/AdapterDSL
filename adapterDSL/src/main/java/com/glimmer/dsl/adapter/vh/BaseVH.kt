package com.glimmer.dsl.adapter.vh

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseVH<BEAN: Any, VB: ViewDataBinding>(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {

    @Throws(Exception::class)
    abstract fun bindData(bean: BEAN, position: Int)

    fun view() = binding.root

    fun context(): Context = view().context

}