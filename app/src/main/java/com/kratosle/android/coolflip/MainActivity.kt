package com.kratosle.android.coolflip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar

class MainActivity : AppCompatActivity(), FlipListener {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flipper = findViewById<CoolFlipView>(R.id.flipper)
        val button = findViewById<Button>(R.id.flipTheCard)
        button.setOnClickListener {
            flipper.flip()
        }
        val seekbar = findViewById<SeekBar>(R.id.seek)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                flipper.flip(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        flipper.flipListener = this
    }

    override fun onFlipStateChanged(state: FlipState, degree: Float) {
        Log.i(TAG, "state: $state  degree:$degree")
    }
}