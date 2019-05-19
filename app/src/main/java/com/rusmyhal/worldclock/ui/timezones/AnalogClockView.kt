package com.rusmyhal.worldclock.ui.timezones

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.rusmyhal.worldclock.R


class AnalogClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var clockColor: Int = Color.BLACK
        set(value) {
            field = value
            updateClockColor()
            invalidate()
        }

    private val dialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val clockStrokeWidth = resources.getDimension(R.dimen.clockStrokeWidth)
    private val clockNumbersSize = resources.getDimension(R.dimen.clockNumbersSize)
    private val clockCenterRadius = resources.getDimension(R.dimen.clockCenterRadius)
    private val textRect = Rect()

    private val numbersList = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")

    private var clockRadius = 0f
    private var padding = 0f
    private var numeralPadding = 0f

    private var handTruncation = 0f
    private var hourHandTruncation = 0f

    private var centerX = 0f
    private var centerY = 0f

    private var isInit = false

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        context.theme.obtainStyledAttributes(attrs, R.styleable.AnalogClockView, 0, 0).also {
            clockColor = it.getColor(R.styleable.AnalogClockView_color, 0)
            it.recycle()
        }

        dialPaint.apply {
            color = clockColor
            strokeWidth = clockStrokeWidth
            style = Paint.Style.STROKE
        }

        textPaint.apply {
            color = clockColor
            textSize = clockNumbersSize
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isInit) {
            centerX = width / 2f
            centerY = height / 2f

            padding = numeralPadding + 50 // spacing from the circle border

            val min = Math.min(width, height)
            clockRadius = min / 2f - padding

            handTruncation = min / 20f
            hourHandTruncation = min / 17f

            isInit = true
        }

        drawCircle(canvas)
        drawCenter(canvas)

        for (hour in numbersList) {
            textPaint.getTextBounds(hour, 0, hour.length, textRect)

            // fund the circle-wise
            val angle: Double = Math.PI / 6 * (hour.toInt() - 3)
            val textX = centerX + Math.cos(angle) * clockRadius - textRect.width() / 2
            val textY = centerY + Math.sin(angle) * clockRadius + textRect.height() / 2
            canvas.drawText(hour, textX.toFloat(), textY.toFloat(), textPaint)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        dialPaint.apply {
            reset()
            color = clockColor
            strokeWidth = clockStrokeWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        canvas.drawCircle(centerX, centerY, clockRadius + padding - 10, dialPaint)
    }

    private fun drawCenter(canvas: Canvas) {
        dialPaint.apply {
            reset()
            color = clockColor
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        canvas.drawCircle(centerX, centerY, clockCenterRadius, dialPaint)
    }

    private fun updateClockColor() {
        dialPaint.color = clockColor
        textPaint.color = clockColor
    }
}