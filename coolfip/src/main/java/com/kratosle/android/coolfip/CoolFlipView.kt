package com.kratosle.android.coolfip

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData


//
// Created by Kamoliddin Valiev on 3/15/23.
// Copyright (c) 2023 KratosLe. All rights reserved.
//


class CoolFlipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {
    private lateinit var frontView: View
    private lateinit var backView: View
    var flipListener: FlipListener? = null
    private var currentFlipState: FlipState = FlipState.Front
    private var flipDegree = MutableLiveData(0f) // 0f <= val <= 180f
    private val lifeCycleObserver = CoolLifeCycleObserver()

    init {
        initProgressListener()

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        validateChildCount()
        locateChildren()
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        validateChildCount()
        super.addView(child, index, params)
        locateChildren()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun validateChildCount() =
        if (childCount > 2) throw IllegalStateException("CoolFlipView can handle only 2 direct children!") else { /*Valid child count*/
        }


    private fun locateChildren() {
        if (childCount < 1) return

        when (childCount) {
            1 -> {
                frontView = getChildAt(0)
                flipListener?.onFlipStateChanged(FlipState.Front, 0f)
            }
            2 -> {
                backView = getChildAt(1)
                backView.scaleY = -1f
                backView.visibility = View.GONE
            }
        }
    }

    fun flip() {
        val va = ValueAnimator.ofFloat(lastPr, if (currentFlipState == FlipState.Front) 180f else 0f)
        va.duration = 1000
        va.addUpdateListener {
            flipDegree.postValue((it.animatedValue as Float).coerceIn(-360f, 360f))
        }
        va.start()
    }

    private var lastPr = 0f
    fun flip(progress: Float) {
        flipDegree.postValue(progress)
    }

    private fun initProgressListener() {
        flipDegree.observe(lifeCycleObserver) { progress ->
            val anim = Rotate3dAnimation(lastPr, progress, 0f, 0f, 0f, 0f)
            anim.duration = 0
            anim.fillAfter = true
            startAnimation(anim)
            lastPr = progress
            currentFlipState = progress.toFlipState()
            when (progress) {
                in 0f..89f -> {
                    frontView.visibility = View.VISIBLE
                    backView.visibility = View.GONE
                }
                in 90f..180f -> {
                    frontView.visibility = View.GONE
                    backView.visibility = View.VISIBLE
                }
            }
            flipListener?.onFlipStateChanged(currentFlipState, progress)
        }
    }

    private fun Float.toFlipState() = when (this) {
        in 0f..89f -> FlipState.Front
        in 90f..180f -> FlipState.Back
        else -> {
            FlipState.Front
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifeCycleObserver.onStart()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifeCycleObserver.onDestroy()
    }

}