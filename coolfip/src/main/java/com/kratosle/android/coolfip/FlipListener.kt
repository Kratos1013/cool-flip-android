package com.kratosle.android.coolfip


//
// Created by Kamoliddin Valiev on 3/15/23.
// Copyright (c) 2023 KratosLe. All rights reserved.
//


interface FlipListener {
    fun onFlipStateChanged(state: FlipState, degree:Float)
}