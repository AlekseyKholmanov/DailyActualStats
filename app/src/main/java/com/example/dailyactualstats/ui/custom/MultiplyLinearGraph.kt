package com.example.dailyactualstats.ui.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.dailyactualstats.R
import com.example.dailyactualstats.extension.dp
import com.example.dailyactualstats.extension.getResColor
import com.example.dailyactualstats.extension.px
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class MultiplyLinearGraph @JvmOverloads constructor(
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
        private var POINT_RADIUS = 2.px.toFloat()
        private var WEEK_PADDING = 30.px
        private var WEEK_DISTANCE = 8.px
        private var GRADUATIONS_BOTTOM_PADDING = 15.px
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
        textSize = 6.px.toFloat()
    }

    private val textPaintBg: Paint = Paint()
    private val textRect: Rect = Rect()

    private var zeroY: Float = 0f
    private var pxPerUnit: Float = 0f
    private var graduationsDistanceScale: Float = 0f
    private var descriptionsDistanceScale: Float = 0f
    private var maxCount = 0

    private var graphValues = mutableMapOf<String,List<Marker>>()
    private var graphsColor = mutableMapOf<String,Int>()
    private var weeks: MutableList<String> = mutableListOf()
    private val graduations: MutableList<Int> = mutableListOf()
    private var step = 0

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

    fun addValue(country:String, values: List<Marker>, color: Int){
        graphValues[country] = values
        graphsColor[country] = color
        initGraduation()

        scaleSpaceToLeaveForGraduations = (width - 2 * GRADUATIONS_SIDE_PADDING) / maxCount
        calcPositions()
        invalidate()
    }

    fun setMarkersAndWeeks(markers: List<Marker>, weeks: List<String>) {

        //descriptionsDistanceScale = (width / weeks.size).toFloat()
        //textPaint.textSize = descriptionsDistanceScale / weeks.size / 1.5f
        initGraduation()
       // initGradient()
        invalidate()
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

        setMeasuredDimension(chartWidth, height + WEEK_PADDING)
    }

    override fun onDraw(c: Canvas) {
        if (graphValues.isEmpty()) {
            return
        }
        drawGradient(c)
        //drawGuidelines(c)
        drawLineAndMarkers(c)
        drawWeeks(c)
        drawGraduations(c)
    }


    private fun initWeeks() {
//        val count = weeks.count()
//        val temp = weeks.take(MAX_WEEKS)
//        weeks.clear()
//        weeks.addAll(temp)
//        if (count > MAX_WEEKS) {
//            weeks.add("${count - MAX_WEEKS}+")
//        }
    }

    private fun initGraduation() {
        var tempMaxValue = 0
        var tempMaxKey = ""
        graphValues.forEach { (key, markers) ->
            val max = markers.maxBy { it.value }!!.value
            val count = markers.count()
            //find max values in Y Axis
            if(max > tempMaxValue){
                tempMaxValue = max
                tempMaxKey = key
            }
            //find max values in X Axis
            if( maxCount > count){
                maxCount = count
            }
        }
        graduations.clear()
        var range = 100
        var forTest = tempMaxValue
        while (forTest / 100 > 1) {
            forTest /= 100
            range *= 10
        }
        for (i in 0..(tempMaxValue / range) + 1) {
            graduations.add(i * range)
        }

        val maxGraduation = graduations.maxBy { it }!!
        val minGraduation = graduations.minBy { it }!!
        pxPerUnit = (chartHeight) / (maxGraduation - minGraduation).toFloat()
        zeroY = maxGraduation * pxPerUnit + paddingTop

        graduationsDistanceScale = ((zeroY) / graduations.size)
    }

    private fun initGradient() {
        gradient =
            LinearGradient(0f, paddingTop.toFloat(), 0f, zeroY, colors, null, Shader.TileMode.CLAMP)
        gradientPaint = Paint().apply {
            style = Paint.Style.FILL
            shader = gradient
            isAntiAlias = true
        }
    }

    private fun calcPositions() {
        step =
            (chartWidth - 2 * GRADUATIONS_SIDE_PADDING) / (maxCount)
        graphValues.forEach { (key, markers) ->
            for ((i, marker) in markers.withIndex()) {
                val x = step * i + paddingLeft
                val y = zeroY - marker.value * pxPerUnit
                marker.currentPos.x = x.toFloat()
                marker.currentPos.y = y
            }
        }
    }

    private fun drawGraduations(c: Canvas) {
        val x = maxCount*step + GRADUATIONS_SIDE_PADDING

        //leave some padding in the bottom
        var step = 0f
        for (value in graduations) {
            val y = zeroY - step
            val formatted = NumberFormat.getIntegerInstance().format(value)
            c.drawText(formatted, x.toFloat(), y, graduationsTextPaint)
            step += graduationsDistanceScale
        }
    }

    private fun drawLineAndMarkers(c: Canvas) {
        graphValues.forEach { (country, markers) ->
            strokePaint.color = graphsColor[country] ?: Color.GRAY
            pointPaint.color = graphsColor[country] ?: Color.GRAY
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
    }

    private fun drawWeeks(c: Canvas) {
//        for ((i, week) in weeks.withIndex()) {
//            textPaint.getTextBounds(week, 0, week.length, textRect)
//            val x = middle(i) + 2 * WEEK_DISTANCE
//            val y = zeroY + 2 * WEEK_DISTANCE
//            val left = x - WEEK_DISTANCE
//            val top = y - textRect.height() - WEEK_DISTANCE
//            val right = x + textRect.width() + WEEK_DISTANCE
//            val bottom = y + WEEK_DISTANCE
//            textRect.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
//            textPaintBg.color = bgColor
//            textPaintBg.style = Paint.Style.FILL
//            c.drawRoundRect(textRect.toRectF(), TEXT_BG_RADIUS, TEXT_BG_RADIUS, textPaintBg)
//            textPaintBg.color = outlineColor
//            textPaintBg.style = Paint.Style.STROKE
//            textPaintBg.strokeWidth = 1.px.toFloat()
//            c.drawRoundRect(textRect.toRectF(), TEXT_BG_RADIUS, TEXT_BG_RADIUS, textPaintBg)
//            c.drawText(week, x, y, textPaint)
//        }
    }

    private fun drawGradient(c: Canvas) {
//        path.reset()
//        path.moveTo(paddingLeft.toFloat(), zeroY)
//        for (marker in markers) {
//            path.lineTo(marker.currentPos.x, marker.currentPos.y)
//        }
//
//        //close path
//        path.lineTo(markers.last().currentPos.x, zeroY)
//        path.lineTo(paddingLeft.toFloat(), zeroY)
//        c.drawPath(path, gradientPaint!!)
    }
}