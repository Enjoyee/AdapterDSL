package com.glimmer.dsl.adapter.ext

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.dsl.adapter.common.CommonListAdapterDsl

fun RecyclerView.attachAdapter(init: AdapterSetup.() -> Unit) {
    val adapterDsl = AdapterSetup(this)
    adapterDsl.init()
}

class AdapterSetup internal constructor(private val recyclerView: RecyclerView) {
    private lateinit var items: MutableList<Any>
    private lateinit var adapter: CommonListAdapterDsl
    private var context: Context = recyclerView.context

    fun dataSource(items: MutableList<Any>) {
        this.items = items
    }

    fun layoutManager(init: AdapterSetup.() -> RecyclerView.LayoutManager) = apply {
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

    fun listItem(init: CommonListAdapterDsl.() -> Unit) {
        this.adapter = CommonListAdapterDsl()
        init.invoke(adapter)
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = adapter
        if (::items.isInitialized) {
            adapter.submitList(items)
        }
    }
}

fun RecyclerView.submitDataSource(items: List<Any>) {
    val list: List<Any> = arrayListOf<Any>().apply { addAll(items) }
    (adapter as? CommonListAdapterDsl)?.submitList(list)
}
