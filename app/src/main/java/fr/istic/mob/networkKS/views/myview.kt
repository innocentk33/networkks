package fr.istic.mob.networkKS.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class myview(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = 800
        val y = 80
        val radius = 40
        val paint = Paint()
        // Use Color.parseColor to define HTML colors
        paint.color = Color.parseColor("#CD5C5C")
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), paint)
    }
}