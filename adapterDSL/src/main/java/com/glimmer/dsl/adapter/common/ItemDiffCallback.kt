package com.glimmer.dsl.adapter.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

open class ItemDiffCallback<BEAN> : DiffUtil.ItemCallback<BEAN>() {
    override fun areItemsTheSame(oldItem: BEAN, newItem: BEAN): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BEAN, newItem: BEAN): Boolean {
        return oldItem == newItem
    }
}