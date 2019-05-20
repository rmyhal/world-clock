package com.rusmyhal.worldclock.ui.timezones

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.rusmyhal.worldclock.R
import java.util.*


class AnalogClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val dialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val clockStrokeWidth = resources.getDimension(R.dimen.clockStrokeWidth)
    private val clockNumbersSize = resources.getDimension(R.dimen.clockNumbersSize)
    private val clockCenterRadius = resources.getDimension(R.dimen.clockCenterRadius)

    private val hoursHandWidth = resources.getDimension(R.dimen.clockHoursHandWidth)
    private val minutesHandWidth = resources.getDimension(R.dimen.clockMinutesHandWidth)
    private val secondsHandWidth = resources.getDimension(R.dimen.clockSecondsHandWidth)
    private val numeralPadding = resources.getDimension(R.dimen.clockNumeralPadding)

    private val numeralRect = Rect()

    private val numbersList = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private val updateRunnable = object : Runnable {
        override fun run() {
            invalidate()
            postDelayed(this, 1000)
        }
    }

    private var clockPadding = 0f
    private var clockRadius = 0f

    private var handTruncation = 0f
    private var hourHandTruncation = 0f

    private var centerX = 0f
    private var centerY = 0f

    private var isInitialized = false

    private var timeZone: TimeZone? = null

    private lateinit var calendar: Calendar

    var clockColor: Int = Color.BLACK
        set(value) {
            field = value
            updateClockColor()
            invalidate()
        }

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
        if (!isInitialized) {
            calculateValues()
            isInitialized = true
        }

        if (timeZone != null) {
            drawCircle(canvas)
            drawCenter(canvas)
            drawNumerals(canvas)
            drawHands(canvas)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(updateRunnable)
    }

    fun setTime(timeZone: String) {
        this.timeZone = TimeZone.getTimeZone(timeZone)

        post(updateRunnable)
    }

    private fun calculateValues() {
        centerX = width / 2f
        centerY = height / 2f

        clockPadding += numeralPadding // clockPadding from the circle border

        val minSideSize = Math.min(width, height)
        clockRadius = minSideSize / 2f - clockPadding

        handTruncation = clockRadius * HAND_TRUNCATION
        hourHandTruncation = clockRadius * HOUR_HAND_TRUNCATION
    }

    private fun drawNumerals(canvas: Canvas) {
        for (hour in numbersList) {
            val tempString = hour.toString()
            textPaint.getTextBounds(tempString, 0, tempString.length, numeralRect)

            val angle: Double = Math.PI / 6 * (hour - 3)
            val textX = centerX + Math.cos(angle) * clockRadius - numeralRect.width() / 2
            val textY = centerY + Math.sin(angle) * clockRadius + numeralRect.height() / 2
            canvas.drawText(tempString, textX.toFloat(), textY.toFloat(), textPaint)
        }
    }

    private fun drawHands(canvas: Canvas) {
        dialPaint.apply {
            reset()
            color = clockColor
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        calendar = Calendar.getInstance(timeZone)
        var hours = calendar.get(Calendar.HOUR_OF_DAY)
        hours = if (hours > 12) hours - 12 else hours
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        val hoursToDraw = (hours + (minutes / 60)) * 5f

        drawHand(canvas, hoursToDraw, HandType.HOUR)
        drawHand(canvas, minutes.toFloat(), HandType.MINUTE)
        drawHand(canvas, seconds.toFloat(), HandType.SECOND)
    }

    private fun drawHand(canvas: Canvas, amount: Float, handType: HandType) {
        val angle = Math.PI * amount / 30 - Math.PI / 2

        dialPaint.strokeWidth = when (handType) {
            HandType.HOUR -> hoursHandWidth
            HandType.MINUTE -> minutesHandWidth
            HandType.SECOND -> secondsHandWidth
        }

        val handLength = if (handType == HandType.HOUR) {
            clockRadius - handTruncation - hourHandTruncation
        } else {
            clockRadius - handTruncation
        }
        val lineStopX = centerX + Math.cos(angle) * handLength
        val lineStopY = centerY + Math.sin(angle) * handLength
        canvas.drawLine(centerX, centerY, lineStopX.toFloat(), lineStopY.toFloat(), dialPaint)
    }


    private fun drawCircle(canvas: Canvas) {
        dialPaint.apply {
            reset()
            color = clockColor
            strokeWidth = clockStrokeWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        canvas.drawCircle(
            centerX,
            centerY,
            clockRadius + clockPadding - clockStrokeWidth,
            dialPaint)
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

    private enum class HandType {
        HOUR,
        MINUTE,
        SECOND
    }

    companion object {

        private const val HAND_TRUNCATION = 0.1f
        private const val HOUR_HAND_TRUNCATION = 0.35f
    }
}