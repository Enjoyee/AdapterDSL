package com.glimmer.dsl.adapter.common

import androidx.databinding.ViewDataBinding
import com.glimmer.dsl.adapter.BasePageListAdapter
import com.glimmer.dsl.adapter.vh.CommonVH
import com.glimmer.dsl.adapter.vh.ViewHolderCreatorDsl

class CommonPageListAdapterDsl : BasePageListAdapter<CommonVH<Any, *>>() {

    fun <BEAN : Any, VB : ViewDataBinding> addItem(resourceId: Int, init: ViewHolderCreatorDsl<BEAN, VB>.() -> Unit) {
        val holder = ViewHolderCreatorDsl<BEAN, VB>(resourceId)
        holder.init()
        typeVHs.put(typeVHs.size(), holder)
    }

}