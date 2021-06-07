package com.glimmer.dsl.adapter.ext

import android.content.Context
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.dsl.adapter.common.CommonPageListAdapterDsl

fun RecyclerView.attachPageAdapter(init: PageAdapterSetup.() -> Unit) {
    val adapterDsl = PageAdapterSetup(this)
    adapterDsl.init()
}

class PageAdapterSetup internal constructor(private val recyclerView: RecyclerView) {
    private lateinit var adapter: CommonPageListAdapterDsl
    private var context: Context = recyclerView.context

    fun layoutManager(init: PageAdapterSetup.() -> RecyclerView.LayoutManager) = apply {
        recyclerView.layoutManager = init().apply {
            if (this is GridLayoutManager) {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        with(adapter) {
                            return typeVHs[getItemViewType(position)]?.spanSize?.invoke() ?: 1
                        }
                    }
                }
            }
        }
    }

    fun listItem(init: CommonPageListAdapterDsl.() -> Unit) {
        this.adapter = CommonPageListAdapterDsl()
        init.invoke(adapter)
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = adapter
    }
}

suspend fun RecyclerView.submitPageDataSource(items: PagingData<Any>) {
    (adapter as? CommonPageListAdapterDsl)?.submitData(items)
}