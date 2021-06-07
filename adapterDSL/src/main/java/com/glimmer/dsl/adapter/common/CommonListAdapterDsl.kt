package com.glimmer.dsl.adapter.common

import androidx.databinding.ViewDataBinding
import com.glimmer.dsl.adapter.BaseListAdapter
import com.glimmer.dsl.adapter.vh.CommonVH
import com.glimmer.dsl.adapter.vh.ViewHolderCreatorDsl

class CommonListAdapterDsl : BaseListAdapter<CommonVH<Any, *>>() {

    fun <BEAN : Any, VB : ViewDataBinding> addItem(resourceId: Int, init: ViewHolderCreatorDsl<BEAN, VB>.() -> Unit) {
        val holder = ViewHolderCreatorDsl<BEAN, VB>(resourceId)
        holder.init()
        typeVHs.put(typeVHs.size(), holder)
    }
}