package com.glimmer.dsl.adapter.decroation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.uutil.getColorById
import com.glimmer.uutil.logE

class GridDividerDecoration(
    context: Context,
    @ColorRes private val color: Int = android.R.color.black,
    private val dividerPx: Int = 1,
) : RecyclerView.ItemDecoration() {

    private val mPaint = Paint()

    init {
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.color = context.getColorById(color)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager == null) {
            return
        }
        drawVertical(c, parent)
        drawHorizontal(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val childCount: Int = parent.childCount
        for (index in 0 until childCount) {
            val child: View = parent.getChildAt(index)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val top = child.top - params.topMargin
            val right: Int = left + dividerPx
            val bottom = child.bottom + params.bottomMargin + dividerPx
            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                mPaint
            )
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val childCount: Int = parent.childCount
        for (index in 0 until childCount) {
            val child: View = parent.getChildAt(index)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            var top = child.bottom + params.bottomMargin
            val right = child.right + params.rightMargin
            val bottom: Int = top + dividerPx

            if (!isLastRow(parent.getChildAdapterPosition(child), parent)) {

            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                mPaint
            )
            }

//            top = child.top + params.bottomMargin - dividerPx
//            canvas.drawRect(
//                left.toFloat(),
//                top.toFloat(),
//                right.toFloat(),
//                bottom.toFloat(),
//                mPaint
//            )
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        var top = 0
        var right = dividerPx
        var bottom = dividerPx
        if (isLastSpan(itemPosition, parent)) {
            right = 0
        }
        if (isFirstRow(itemPosition, parent)) {
            top = dividerPx
        }
        if (isLastRow(itemPosition, parent)) {
            "最后".logE()
            bottom = 0
        }
        outRect.set(0, top, right, bottom)
    }

    private fun isFirstRow(itemPosition: Int, parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            if (itemPosition < spanCount) return true
        }
        return false
    }

    private fun isLastRow(itemPosition: Int, parent: RecyclerView): Boolean {
//        val layoutManager = parent.layoutManager
//        if (layoutManager is GridLayoutManager) {
//            val spanCount = layoutManager.spanCount
//            val itemCount: Int = layoutManager.getItemCount()
//            if (itemCount - itemPosition - 1 < spanCount) return true
//        }
//        return false

        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val itemCount: Int = layoutManager.getItemCount()
            val spanCount = layoutManager.spanCount
            if (itemPosition > itemCount - spanCount) return true
        }
        return false
    }

    private fun isLastSpan(itemPosition: Int, parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            if ((itemPosition + 1) % spanCount == 0) return true
        }
        return false
    }

}