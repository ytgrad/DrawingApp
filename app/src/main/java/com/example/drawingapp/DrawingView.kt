package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var drawingPath: CustomPath? = null
    private var canvasBitmap: Bitmap? = null
    private var drawPaint: Paint? = null
    private var canvasPaint: Paint? = null
    private var brushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private var paths = ArrayList<CustomPath>()
    private var undopaths = ArrayList<CustomPath>()

    fun onUndoClick(){
        if(paths.size > 0){
            undopaths.add(paths.removeAt(paths.size - 1))
            invalidate()
        }
    }

    fun onRedoClick(){
        if(undopaths.size > 0){
            paths.add(undopaths.removeAt(undopaths.size - 1))
            invalidate()
        }
    }

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        drawPaint = Paint()
        drawingPath = CustomPath(color, brushSize)
        drawPaint!!.color = color
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
        //brushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)

        for(path in paths){
            drawPaint!!.strokeWidth = path.brushThickness
            drawPaint!!.color = path.color
            canvas.drawPath(path, drawPaint!!)
        }

        if(!drawingPath!!.isEmpty){
            drawPaint!!.strokeWidth = drawingPath!!.brushThickness
            drawPaint!!.color = drawingPath!!.color
            canvas.drawPath(drawingPath!!, drawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                drawingPath!!.brushThickness = brushSize
                drawingPath!!.color = color

                drawingPath!!.reset()
                drawingPath!!.moveTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_MOVE ->{
                drawingPath!!.lineTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_UP ->{
                paths.add(drawingPath!!)
                drawingPath = CustomPath(color, brushSize)
            }
            else -> return false
        }
        invalidate()

        return true
    }

    public fun setBrushSize(newSize: Float){
        brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)
        drawPaint!!.strokeWidth = brushSize
    }

    fun setColor(newColor: String){
        Log.e("clicked", newColor)
        this.color = Color.parseColor(newColor)
       // drawPaint!!.color = color
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float): Path() {

    }

}