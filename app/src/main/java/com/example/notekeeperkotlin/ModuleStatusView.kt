package com.example.notekeeperkotlin

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * TODO: document your custom view class.
 */
class ModuleStatusView : View {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    private var moduleStatus: Array<Boolean> = Array<Boolean>(5){false}
        get() = field
        set(value) {
            field = value
        }

    private var outlineWidth = 0f
    var shapeSize = 0f
    var spacing = 0f
    private val outlineColor = Color.BLACK
    var paintOutline = Paint()
    var paintFill = Paint()
    private val moduleRectangles: Array<Rect> = Array<Rect>(moduleStatus.size){ Rect() }
    private var radius: Float = 0f
    private val editModeCount = 7
    var maxHorizontalModules = 0


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }




    private fun init(attrs: AttributeSet?, defStyle: Int) {

        if(isInEditMode) {
            setUpEditMOdeValues()
        }
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ModuleStatusView, defStyle, 0
        )


        a.recycle()

        outlineWidth = 6f
        shapeSize = 144f
        spacing = 30f

        radius = (shapeSize - outlineWidth) * 2



        paintOutline = Paint(Paint.ANTI_ALIAS_FLAG)
        paintOutline.style = Paint.Style.STROKE
        paintOutline.strokeWidth = outlineWidth
        paintOutline.color = outlineColor

        val fillColour = context.resources.getColor(R.color.pluralsightorange)

        paintFill = Paint(Paint.ANTI_ALIAS_FLAG)
        paintFill.style = Paint.Style.FILL
        paintFill.color = fillColour


    }

    private fun setUpEditMOdeValues() {
        val exampleModuleValues = Array<Boolean>(editModeCount){i -> false}

        val middle = editModeCount / 2

        for (i in 0..middle) {
            exampleModuleValues[i] = true
        }

        moduleStatus = exampleModuleValues
    }

    private fun setUpModuleRectangles(w: Int) {

        val availablewidth = w - paddingLeft - paddingRight
        val horizontalPossibleFit  : Int = (availablewidth / (shapeSize + spacing)).toInt()
        val maximumHorizontalModules = horizontalPossibleFit.coerceAtMost(moduleStatus.size)
        for (i in moduleStatus.indices) {
            var row = 1
            var column = 1
            if( i != 0){
                row = i / maximumHorizontalModules
                column = i % maximumHorizontalModules
            }


            val x : Int = (paddingLeft + (shapeSize +spacing)*column).toInt()
            val y : Int = (paddingTop + row * (shapeSize + spacing)).toInt()

            moduleRectangles[i] = Rect(x, y, x + shapeSize.toInt(), y + shapeSize.toInt())
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var desiredWidth = 0
        var desiredHeight = 0

        val specWidth = MeasureSpec.getSize(widthMeasureSpec)

        val availableWidth = specWidth - paddingLeft -paddingRight

        val horizontalPossibleFit  : Int = (availableWidth / (shapeSize + spacing)).toInt()


        maxHorizontalModules = horizontalPossibleFit.coerceAtMost(moduleStatus.size)

        desiredWidth = (maxHorizontalModules * (shapeSize  + spacing) - spacing).toInt()
        desiredWidth += paddingLeft + paddingRight


        val rows = ((moduleStatus.size - 1) / maxHorizontalModules) + 1
        desiredHeight = (((shapeSize + spacing)*rows)-spacing).toInt()
        desiredHeight += paddingBottom + paddingTop

        val width = resolveSizeAndState(desiredWidth,widthMeasureSpec,0)
        val height = resolveSizeAndState(desiredHeight,heightMeasureSpec,0)

        setMeasuredDimension(width,height)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        setUpModuleRectangles(w)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        for (i in moduleRectangles.indices) {
            val x  : Float= moduleRectangles[i].exactCenterX()
            val y  : Float = moduleRectangles[i].exactCenterY()





            if (moduleStatus[i]) {
                canvas.drawCircle(x, y.toFloat(), radius, paintFill)
            }


            canvas.drawCircle(x.toFloat(), y.toFloat(), radius, paintOutline)

        }
    }
}