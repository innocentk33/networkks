package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawEngine(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    val paint = Paint() // permet de dessiner des formes
    var rect = RectF() // permet de dessiner un rectangle arrondi avec drawRoundRect a la position top, left, right, bottom
    var position = PointF() // permet de dessiner un rectangle arrondi a la position x,y
    private val cornerRadius = 10f // Rayon des coins en pixels
    private val rectWidth = 150f
    private val rectHeight = 100f
    private var rectangles = ArrayList<RectF>()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        //Grace a cette fonction on peut dessiner sur la vue un rectangle arrondi a la position 100,100
        // Définir le rectangle arrondi à la position spécifiée
        // Rectangle arrondi
        //canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        // dessine tous les rectangles arrondis de la liste rectangles
        for (rect in rectangles) {
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //desine un rectangle arrondi a la position du doigt quand on touche l'ecran dans la drawZone
        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                position.x = event.x
                position.y = event.y
                val newPositionx = event.x
                val newPositiony = event.y
                val newRect = RectF(newPositionx, newPositiony, newPositionx + rectWidth, newPositiony + rectHeight)
               // rect.set(position.x, position.y, position.x + rectWidth, position.y + rectHeight)
                rectangles.add(newRect)
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    // permet de vide la liste rectangles et de redessiner la vue
    fun clearRectangles(){
        rectangles.clear()
        invalidate()
    }

}