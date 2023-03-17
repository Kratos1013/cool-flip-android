package com.kratosle.android.coolflip

import android.graphics.Camera
import android.graphics.Matrix
import android.util.Log
import android.view.animation.Animation
import android.view.animation.Transformation


//
// Created by Kamoliddin Valiev on 3/16/23.
// Copyright (c) 2023 KratosLe. All rights reserved.
//


class Rotate3dAnimation(
    private val fromXDegrees: Float,
    private val toXDegrees: Float,
    private val fromYDegrees: Float,
    private val toYDegrees: Float,
    private val fromZDegrees: Float,
    private val toZDegrees: Float
) :
    Animation() {
    private lateinit var camera: Camera
    private var width = 0
    private var height = 0

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        this.width = width / 2
        this.height = height / 2
        camera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val xDegrees = fromXDegrees + (toXDegrees - fromXDegrees) * interpolatedTime
        val yDegrees = fromYDegrees + (toYDegrees - fromYDegrees) * interpolatedTime
        val zDegrees = fromZDegrees + (toZDegrees - fromZDegrees) * interpolatedTime
        val matrix: Matrix = t.matrix
        camera.save()
        camera.rotateX(xDegrees)
        camera.rotateY(yDegrees)
        camera.rotateZ(zDegrees)
        camera.setLocation(0f,0f,30f)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-width.toFloat(), -height.toFloat())
        matrix.postTranslate(width.toFloat(), height.toFloat())
    }
}