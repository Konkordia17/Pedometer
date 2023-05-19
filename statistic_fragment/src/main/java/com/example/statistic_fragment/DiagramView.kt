package com.example.statistic_fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dataPoints: List<Float> = listOf(50f, 100f, 75f, 120f, 90f, 150f, 300f)
    private val barColor: Int = Color.BLUE

    private fun paint(item: Float): Paint = Paint().apply {
        val map = getBarColor(dataPoints).associate { it.first to it.second }

        val colorMap = dataPoints.associateWith { map[it] }
        color = colorMap[item] ?: barColor

        style = Paint.Style.FILL
    }

    private val gridPaint: Paint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val widthPerBar = width.toFloat() / 14

        drawPaper(paint = gridPaint, canvas = canvas)
        drawColumn(widthPerBar, canvas)
    }


    private fun drawPaper(paint: Paint, canvas: Canvas) {
        val mYUnit = height / 10f
        val mXUnit = width / 14f

        paint.strokeWidth = 1f

        var cx = 0f
        for (i in 0..14) {
            canvas.drawLine(cx, 0f, cx, height.toFloat(), paint)
            cx += mXUnit
        }

        var cy: Float = height.toFloat()
        for (i in 0..10) {
            canvas.drawLine(0f, cy, width.toFloat(), cy, paint)
            cy -= mYUnit
        }
    }

    private fun drawColumn(widthPerBar: Float, canvas: Canvas) {
        val maxDataPoint: Float = dataPoints.maxOrNull() ?: 0f
        for ((index, dataPoint) in dataPoints.withIndex()) {
            var pass = index * 2
            val barHeight = ((dataPoint / maxDataPoint) * height) / 10 * 9
            val left = pass * widthPerBar
            val right = left + widthPerBar
            val bottom = height.toFloat()
            val top = bottom - barHeight


            canvas.drawRect(left, top, right, bottom, paint(dataPoint))
        }
    }

    private fun getBarColor(list: List<Float>): List<Pair<Float, Int>> {
        val colorList = list.sorted().mapIndexed { index, value ->
            val color = getColorForIndex(index)
            Pair(value, color)
        }
        return colorList
    }

    private fun getColorForIndex(index: Int): Int {
        return when (index) {
            0 -> Color.DKGRAY
            1 -> Color.GRAY
            2 -> Color.CYAN
            3 -> Color.GREEN
            4 -> Color.YELLOW
            5 -> Color.RED
            else -> Color.MAGENTA
        }
    }
}

