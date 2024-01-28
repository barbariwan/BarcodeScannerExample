package com.devpulsar.barcodescannerexample.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PunchHoleView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        color = Color.BLACK
        strokeWidth = 10f
        isAntiAlias = true
    }
    private val borderPath = Path()
    private val roundRectF by lazy { RectF(0f, 0f, width.toFloat(), height.toFloat()) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        borderPath.addRoundRect(roundRectF, 20f, 20f, Path.Direction.CW)
        canvas.clipPath(borderPath)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), clearPaint)
    }
}