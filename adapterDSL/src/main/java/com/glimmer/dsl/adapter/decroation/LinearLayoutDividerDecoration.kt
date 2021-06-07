package com.glimmer.dsl.adapter.decroation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.uutil.density
import com.glimmer.uutil.getColorById
import kotlin.math.roundToInt

class LinearLayoutDividerDecoration(
    private val context: Context,
    @ColorRes private val color: Int = android.R.color.black,
    private val dividerPx: Int = 1,
    private val orientation: Int = LinearLayout.VERTICAL
) : RecyclerView.ItemDecoration() {

    private val mBounds = Rect()
    private val mMaiginBounds = Rect()
    private val mPaint = Paint()
    private var mNeedFirstDivider = true
    private var mNeedLastDivider = true

    init {
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.color = context.getColorById(color)
    }

    fun setHorizontalDividerMargin(topDp: Int = 0, bottomDp: Int = 0) {
        mMaiginBounds.top = (topDp * context.density).roundToInt()
        mMaiginBounds.bottom = (bottomDp * context.density).roundToInt()
    }

    fun setVerticalDividerMargin(leftDp: Int = 0, rightDp: Int = 0) {
        mMaiginBounds.left = (leftDp * context.density).roundToInt()
        mMaiginBounds.right = (rightDp * context.density).roundToInt()
    }

    fun needFirstDivider(need: Boolean) {
        mNeedFirstDivider = need
    }

    fun needLastDivider(need: Boolean) {
        mNeedLastDivider = need
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager == null) {
            return
        }
        if (orientation == LinearLayout.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            var bottom = mBounds.bottom + child.translationY.roundToInt()
            var top = bottom - dividerPx
            val finalLeft = left + mMaiginBounds.left
            val finalRight = right + mMaiginBounds.right

            (parent.layoutManager as? LinearLayoutManager)?.apply {
                if (mNeedLastDivider || (parent.getChildAdapterPosition(child) != findLastCompletelyVisibleItemPosition())) {
                    canvas.drawRect(finalLeft.toFloat(), top.toFloat(), finalRight.toFloat(), bottom.toFloat(), mPaint)
                }
            }

            if (mNeedFirstDivider && parent.getChildAdapterPosition(child) == 0) {
                bottom = child.top + child.translationY.roundToInt()
                top = bottom - dividerPx
                canvas.drawRect(finalLeft.toFloat(), top.toFloat(), finalRight.toFloat(), bottom.toFloat(), mPaint)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + child.translationX.roundToInt()
            val left = right - dividerPx
            val finalTop = top + mMaiginBounds.top
            val finalBottom = bottom + mMaiginBounds.bottom
            canvas.drawRect(left.toFloat(), finalTop.toFloat(), right.toFloat(), finalBottom.toFloat(), mPaint)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (orientation == LinearLayout.VERTICAL) {
            if (shouldDrawBottomDivider(parent)) {
                outRect.bottom = dividerPx
            } else {
                outRect.bottom = 0
            }

            // 第一个item的顶部分割线
            if (mNeedFirstDivider && parent.getChildAdapterPosition(view) == 0) {
                outRect.top = dividerPx
            }
        } else {
            outRect[0, 0, dividerPx] = 0
        }
    }

    private fun shouldDrawBottomDivider(parent: RecyclerView): Boolean {
        (parent.layoutManager as? LinearLayoutManager)?.apply {
            return !(!mNeedLastDivider && itemCount - 1 == findLastCompletelyVisibleItemPosition())
        }
        return false
    }

}