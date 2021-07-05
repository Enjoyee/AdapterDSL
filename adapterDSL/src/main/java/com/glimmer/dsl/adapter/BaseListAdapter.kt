package com.glimmer.dsl.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.dsl.adapter.common.ItemDiffCallback
import com.glimmer.dsl.adapter.vh.BaseVH
import com.glimmer.dsl.adapter.vh.ViewHolderCreatorDsl
import com.glimmer.uutil.doWithTry
import kotlin.reflect.KClass

abstract class BaseListAdapter<VH : BaseVH<Any, *>>(callback: ItemDiffCallback<Any> = ItemDiffCallback()) : ListAdapter<Any, VH>(callback) {
    internal val typeVHs = SparseArrayCompat<ViewHolderCreatorDsl<*, *>>()
    internal val clsBeanType = SparseArrayCompat<KClass<*>>()

    /**==========================================================**/
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        // DSL传参<优先>
        typeVHs[viewType]?.let { vh ->
            return vh.createVH(parent) as VH
        }
        // 外部直接实现创建VH方式
        createVH(parent, viewType)?.let { return it as VH }

        throw NullPointerException("onCreateViewHolder Exception：No ItemViewType matches type $viewType in data source")
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { bean ->
            doWithTry { holder.bindData(bean, position) }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        // DSL传参<优先>
        for (index in 0 until typeVHs.size()) {
            typeVHs[index]?.let { vh ->
                if (vh.isViewType.invoke(getItem(position))) {
                    return index
                }
            }
        }
        // 外部直接实现ViewType方式
        forViewType(getItem(position), position)?.let { return it }

        throw NullPointerException("getItemViewType Exception：No holder added that matches at position=$position in data source")
    }

    /**==========================================================**/

    fun getItemData(position: Int) = getItem(position)

    open fun forViewType(bean: Any, position: Int): Int? = null

    open fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? = null

    /**==========================================================**/
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        typeVHs.clear()
    }

}
