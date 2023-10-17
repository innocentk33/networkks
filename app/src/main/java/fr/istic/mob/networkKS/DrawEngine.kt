package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DrawEngine(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
        private  val paint = Paint()
        private val rect = RectF()
    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        //Grace a cette fonction on peut dessiner sur la vue un rectangle arrondi a la position 100,100
        val width = 200f
        val height = 150f
        // Paramètres du rectangle arrondi
        val cornerRadius = 10f // Rayon des coins en pixels
        // Rectangle arrondi
        rect.set(width/2f, height/2f, width, height)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
    }

}