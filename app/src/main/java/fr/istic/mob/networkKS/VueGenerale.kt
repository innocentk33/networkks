package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class VueGenerale(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr, defStyleRes) {



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    /*    val x = 800
        val y = 0
        val radius = 40
        val paint = Paint()
        // Use Color.parseColor to define HTML colors
        paint.color = Color.parseColor("#CD5C5C")
        canvas?.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), paint)*/
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return super.onGenericMotionEvent(event)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }
}
