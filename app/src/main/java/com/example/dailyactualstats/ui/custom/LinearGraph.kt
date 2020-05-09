package com.example.dailyactualstats.ui.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRectF
import com.example.dailyactualstats.R
import com.example.dailyactualstats.extension.getResColor
import com.example.dailyactualstats.extension.px
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class LinearGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {


    companion object {
        private const val MAX_WEEKS = 4
        private var INTERVAL = 5.px.toFloat()
        private var DOTTED_STROKE_WIDTH_DP = 1.px.toFloat()
        private var STROKE_WIDTH_DP = 1.px.toFloat()
        private var TEXT_BG_RADIUS = 8.px.toFloat()
        private var POINT_RADIUS = 3.px.toFloat()
        private var WEEK_PADDING = 30.px
        private var WEEK_DISTANCE = 8.px
        private var GRADUATIONS_BOTTOM_PADDING = 10.px
        private var GRADUATIONS_SIDE_PADDING = 10.px
    }

    private var _colorStart: Int = context.getResColor(R.color.colorPrimary)
    var colorStart
        get() = _colorStart
        set(value) {
            _colorStart = value
            colors = intArrayOf(colorStart, colorEnd)
            invalidate()
        }

    private var _colorEnd: Int = Color.WHITE
    var colorEnd
        get() = _colorEnd
        set(value) {
            _colorEnd = value
            colors = intArrayOf(colorStart, colorEnd)
            invalidate()
        }

    private var _lineColor: Int = context.getResColor(R.color.colorPrimary)
    var lineColor
        get() = _lineColor
        set(value) {
            _lineColor = value
            invalidate()
        }

    private var _dottedColor: Int = Color.GRAY
    var dottedLineColor
        get() = _dottedColor
        set(value) {
            _dottedColor = value
            invalidate()
        }

    //prepare gradient path
    private var _colors = intArrayOf(colorStart, colorEnd)
    var colors
        get() = _colors
        set(value) {
            _colors = value
            invalidate()
        }

    private var _bgColor = Color.WHITE
    var bgColor
        get() = _bgColor
        set(value) {
            _bgColor = value
            invalidate()
        }

    private var _outlineColor = Color.DKGRAY
    var outlineColor
        get() = _outlineColor
        set(value) {
            _outlineColor = value
            invalidate()
        }

    private val graduations: MutableList<Int> = mutableListOf()
    private var gradient: LinearGradient? = null
    private var gradientPaint: Paint? = null

    private val dottedPaint: Paint
    private val strokePaint: Paint
    private val pointPaint: Paint

    private val textPaint: TextPaint = TextPaint().apply {
        this.color = Color.BLUE
        this.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        isAntiAlias = true
    }
    private val graduationsTextPaint: TextPaint = TextPaint().apply {
        this.color = Color.BLUE
        this.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        isAntiAlias = true
    }

    private val textPaintBg: Paint = Paint()
    private val textRect: Rect = Rect()

    private var zeroY: Float = 0f
    private var pxPerUnit: Float = 0f
    private var graduationsDistanceScale: Float = 0f
    private var weeksDistanceScale: Float = 0f

    private var markers: List<Marker> = mutableListOf()
    private var weeks: MutableList<String> = mutableListOf()

    private val path: Path = Path()
    private val guidelinePath: Path = Path()

    private var chartHeight: Int = 0
    private var chartWidth: Int = 0
    private var scaleSpaceToLeaveForGraduations: Int = 0


    init {
        dottedPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = DOTTED_STROKE_WIDTH_DP
            pathEffect = DashPathEffect(floatArrayOf(INTERVAL, INTERVAL), 0f)
            isAntiAlias = true
            color = dottedLineColor
        }
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH_DP
            isAntiAlias = true
            color = lineColor
        }
        pointPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = lineColor
        }
    }

    fun setMarkersAndWeeks(markers: List<Marker>, weeks: List<String>) {
        this.weeks = weeks.toMutableList()
        this.markers = markers
        initGraduation()
        initWeeks()
    }

    private fun initWeeks() {
        val count = weeks.count()
        val temp = weeks.take(MAX_WEEKS)
        weeks.clear()
        weeks.addAll(temp)
        if (count > MAX_WEEKS) {
            weeks.add("${count - MAX_WEEKS}+")
        }
    }

    private fun initGraduation() {
        val max = markers.maxBy { it.value }!!
        if (max.value <= 100) {
            graduations.add(0)
            graduations.add(100)
        }
        var start = max.value - max.value % 100
        while (start >= 0) {
            graduations.add(start)
            start -= 100
        }
        graduations.reverse()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = paddingLeft + paddingRight
        var height = paddingTop + paddingBottom
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width += 200.px
            width = max(width, suggestedMinimumWidth)
            if (widthMeasureSpec == MeasureSpec.AT_MOST) width = min(widthSize, width)
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height += 200.px
            height = max(height, suggestedMinimumHeight)
            if (heightMode == MeasureSpec.AT_MOST) height = min(height, heightSize)
        }

        //compensate for the week text padding
        chartHeight = height - WEEK_PADDING
        chartWidth = width
        scaleSpaceToLeaveForGraduations = (width - 2 * GRADUATIONS_SIDE_PADDING) / markers.size
        weeksDistanceScale = (width / weeks.size).toFloat()
        graduationsDistanceScale = (height / graduations.size) * 0.8.toFloat()
        textPaint.textSize = weeksDistanceScale / weeks.size / 1.5f
        graduationsTextPaint.textSize = scaleSpaceToLeaveForGraduations * 0.4.toFloat()
        calcAndInvalidate()
        setMeasuredDimension(chartWidth, height + WEEK_PADDING)
    }

    private fun calcAndInvalidate() {
        calcPositions(markers)
        initGradient()
        invalidate()
    }

    private fun initGradient() {
        gradientPaint = Paint().apply {
            style = Paint.Style.FILL
            shader = gradient
            isAntiAlias = true
        }
        gradient =
            LinearGradient(0f, paddingTop.toFloat(), 0f, zeroY, colors, null, Shader.TileMode.CLAMP)
    }

    override fun onDraw(c: Canvas) {
        drawGradient(c)
        drawGuidelines(c)
        drawLineAndMarkers(c)
        drawWeeks(c)
        drawGraduations(c)
    }

    private fun drawGraduations(c: Canvas) {
        val x = markers.last().currentPos.x + GRADUATIONS_SIDE_PADDING
        //leave some padding in the bottom
        var step = 0f + GRADUATIONS_BOTTOM_PADDING
        for (value in graduations) {
            val y = zeroY - step
            val formatted = NumberFormat.getIntegerInstance().format(value)
            c.drawText(formatted, x, y, graduationsTextPaint)
            step += graduationsDistanceScale
        }
    }

    private fun drawLineAndMarkers(c: Canvas) {
        var previousMarker: Marker? = null
        for (marker in markers) {
            if (previousMarker != null) {
                //draw line
                val p1 = previousMarker.currentPos
                val p2 = marker.currentPos
                c.drawLine(p1.x, p1.y, p2.x, p2.y, strokePaint)
            }
            previousMarker = marker
            //draw marker
            c.drawCircle(marker.currentPos.x, marker.currentPos.y, POINT_RADIUS, pointPaint)

        }
    }

    private fun drawWeeks(c: Canvas) {
        for ((i, week) in weeks.withIndex()) {
            textPaint.getTextBounds(week, 0, week.length, textRect)
            val x = middle(i) + 2 * WEEK_DISTANCE
            val y = zeroY + 2 * WEEK_DISTANCE
            val left = x - WEEK_DISTANCE
            val top = y - textRect.height() - WEEK_DISTANCE
            val right = x + textRect.width() + WEEK_DISTANCE
            val bottom = y + WEEK_DISTANCE
            textRect.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            textPaintBg.color = bgColor
            textPaintBg.style = Paint.Style.FILL
            c.drawRoundRect(textRect.toRectF(), TEXT_BG_RADIUS, TEXT_BG_RADIUS, textPaintBg)
            textPaintBg.color = outlineColor
            textPaintBg.style = Paint.Style.STROKE
            textPaintBg.strokeWidth = 1.px.toFloat()
            c.drawRoundRect(textRect.toRectF(), TEXT_BG_RADIUS, TEXT_BG_RADIUS, textPaintBg)
            c.drawText(week, x, y, textPaint)
        }
    }

    private fun middle(i: Int): Float {
        return (i * (chartWidth / weeks.count())).toFloat()
    }

    private fun drawGuidelines(c: Canvas) {
        for (i in 0..markers.lastIndex step 3) {
            val marker = markers[i]
            guidelinePath.reset()
            guidelinePath.moveTo(marker.currentPos.x, paddingTop.toFloat())
            guidelinePath.lineTo(marker.currentPos.x, zeroY)
            c.drawPath(guidelinePath, dottedPaint)
        }
    }

    private fun drawGradient(c: Canvas) {
        path.reset()
        path.moveTo(paddingLeft.toFloat(), zeroY)
        for (marker in markers) {
            path.lineTo(marker.currentPos.x, marker.currentPos.y)
        }

        //close path
        path.lineTo(markers.last().currentPos.x, zeroY)
        path.lineTo(paddingLeft.toFloat(), zeroY)
        c.drawPath(path, gradientPaint!!)
    }

    private fun calcPositions(markers: List<Marker>) {
        val max = markers.maxBy { it.value }!!
        val min = markers.minBy { it.value }!!
        pxPerUnit = (chartHeight) / (max.value - min.value).toFloat()
        zeroY = max.value * pxPerUnit + paddingTop

        val step =
            (chartWidth - 2 * GRADUATIONS_SIDE_PADDING - scaleSpaceToLeaveForGraduations) / (markers.size - 1)
        for ((i, marker) in markers.withIndex()) {
            val x = step * i + paddingLeft
            val y = zeroY - marker.value * pxPerUnit
            marker.currentPos.x = x.toFloat()
            marker.currentPos.y = y
        }
    }
//
//    private fun drawGuidelines(canvas: Canvas) {
//        for (i in 0..points.lastIndex step 7) {
//            val point = points[i]
//            guidelinesPath.reset()
//            guidelinesPath.moveTo(point.x, paddingTop.toFloat())
//            guidelinesPath.lineTo(point.x, zeroY)
//            canvas.drawPath(guidelinesPath, dottedPaint)
//        }
//    }
//
//    private fun calcPosition(markers: List<Float>) {
//        val max = markers.maxBy { it }!!
//        val min = markers.minBy { it }!!
//        val pxPerUnit = height / (max - min)
//        zeroY = max * pxPerUnit + marginTop
//
//        val step = (width - 2 * padding - scaleWidth) / (markers.size - 1)
//        for ((i, marker) in markers.withIndex()) {
//            val x = step * i + paddingLeft
//            val y = zeroY - marker * pxPerUnit
//            points.add(Point(x, y))
//        }
//    }
//
//    private fun drawGradient(canvas: Canvas) {
//        path.reset()
//        path.moveTo(paddingLeft.toFloat(), zeroY)
//        for (point in points) {
//            path.lineTo(point.x, point.y)
//        }
//        path.lineTo(points.last().x, zeroY)
//        path.lineTo(paddingLeft.toFloat(), zeroY)
//
//
//        canvas.drawPath(path, gradientPaint)
//    }
//
//    private fun drawLineAndMarkers(canvas: Canvas) {
//        var previousPoint: Point? = null
//        for (point in points) {
//            if (previousPoint != null) {
//                canvas.drawLine(previousPoint.x, previousPoint.y, point.x, point.y, strokePaint)
//            }
//            previousPoint = point
//            canvas.drawCircle(point.x, point.y, 4f, pointPaint)
//        }
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        drawGradient(canvas)
//        drawGuidelines(canvas)
//        drawLineAndMarkers(canvas)
//
//    }
}

data class Marker(val currentPos: CurrentPosition = CurrentPosition(0f, 0f), val value: Int = 0)

data class CurrentPosition(var x: Float, var y: Float)