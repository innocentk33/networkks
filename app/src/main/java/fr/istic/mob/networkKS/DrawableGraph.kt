package fr.istic.mob.networkKS

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View

class DrawableGraph :Drawable() {
    override fun draw(canvas: Canvas) {
       // TODO("Not yet implemented")
        canvas.drawCircle(100f,100f,40f, Paint())
    }
    override fun setAlpha(alpha: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        TODO("Not yet implemented")
    }
}
