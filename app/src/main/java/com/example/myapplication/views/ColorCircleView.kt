package com.example.myapplication.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R


class ColorCircleView : View {
    private var _color = 0
    private var _radius = 0f

    constructor(context: Context, color: Int) : super(context) {
        init(null, 0)
        _color = color
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val colorSize = resources.getDimension(R.dimen.color_circle_size).toInt()
        _radius = colorSize / 2f
        invalidate()
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ColorCircle, defStyle, 0
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = _color
        canvas.drawCircle(_radius, _radius, _radius, paint)
    }
}
