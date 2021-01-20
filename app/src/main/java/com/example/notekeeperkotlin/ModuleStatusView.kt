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

    private var moduleStatus: Array<Boolean?> = arrayOf()

    private var outlineWidth = 0f
    var shapeSize = 0f
    var spacing = 0f
    val outlineColor = Color.BLACK
    var paintOutline = Paint()
    var paintFill = Paint()
    private val moduleRectangles: Array<Rect> = arrayOf()
    private var radius: Float = 0f
    private val editModeCount = 7


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

        if (isInEditMode) {
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

        setUpModuleRectangles()

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
        val exampleModuleValues = arrayOfNulls<Boolean>(editModeCount)

        val middle = editModeCount / 2

        for (i in 0..middle) {
            exampleModuleValues[i] = true
        }

        moduleStatus = exampleModuleValues
    }

    private fun setUpModuleRectangles() {
        for (i in 0..moduleStatus.size) {
            val x = (shapeSize + spacing) * i
            val y = 0

            moduleRectangles[i] = Rect(x.toInt(), y, shapeSize.toInt(), (y + shapeSize).toInt())
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        for (i in 0..moduleRectangles.size) {
            val x = moduleRectangles[i].centerX()
            val y = moduleRectangles[i].centerY()




            if (moduleStatus[i] == true) {
                canvas.drawCircle(x.toFloat(), y.toFloat(), radius, paintFill)
            }

            canvas.drawCircle(x.toFloat(), y.toFloat(), radius, paintOutline)
        }
    }
}