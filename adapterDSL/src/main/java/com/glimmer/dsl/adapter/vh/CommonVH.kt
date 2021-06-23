package com.glimmer.dsl.adapter.vh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.glimmer.uutil.Clicker

class CommonVH<BEAN : Any, VB : ViewDataBinding>(
    viewGroup: ViewGroup,
    @LayoutRes viewLayout: Int,
    viewDataBinding: VB = createVHBinding(
        viewGroup,
        viewLayout
    )
) : BaseVH<BEAN, VB>(viewDataBinding), Clicker {
    val vhDataBinding = viewDataBinding
    private var beanVariableId: Int? = null
    private lateinit var bean: BEAN
    private var itemPosition: Int = 0
    private var setUp: ((bean: BEAN, position: Int) -> Unit)? = null
    private var clicker: ((View, BEAN, Int) -> Unit)? = null

    private fun bindVariable(variableId: Int?, value: Any) {
        variableId?.let { vhDataBinding.setVariable(it, value) }
    }

    override fun bindData(bean: BEAN, position: Int) {
        this.bean = bean
        this.itemPosition = position
        bindVariable(beanVariableId, bean)
        setUp?.invoke(bean, position)
    }

    override fun onClick(v: View?) {
        v?.let {
            clicker?.invoke(it, bean, itemPosition)
            bindingAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * ===============================================================
     */
    fun setData(setUp: ((bean: BEAN, position: Int) -> Unit)?) {
        this.setUp = setUp
    }

    fun bindData(
        beanVariableId: Int,
        setUp: ((bean: BEAN, position: Int) -> Unit)? = null
    ) {
        this.beanVariableId = beanVariableId
        this.setUp = setUp
    }

    fun clicker(clickerVariableId: Int, clicker: ((View, BEAN, Int) -> Unit)?) {
        bindVariable(clickerVariableId, this)
        this.clicker = clicker
    }

    fun refreshItem(bean: BEAN = this.bean) {
        bindVariable(beanVariableId, bean)
    }

}

/**
 * ==========================================================================================
 */
fun <VB : ViewDataBinding> createVHBinding(viewGroup: ViewGroup, @LayoutRes viewLayout: Int) =
    requireNotNull(
        DataBindingUtil.bind<VB>(
            inflateView(
                viewGroup,
                viewLayout
            )
        )
    ) { "create vh data binding err." }

fun inflateView(viewGroup: ViewGroup, @LayoutRes viewLayout: Int): View {
    val layoutInflater = LayoutInflater.from(viewGroup.context)
    return layoutInflater.inflate(viewLayout, viewGroup, false)
}
