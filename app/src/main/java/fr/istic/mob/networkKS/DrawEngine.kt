package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import fr.istic.mob.networkKS.models.Graph
import fr.istic.mob.networkKS.models.Objet

class DrawEngine(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    /*
        val paint = Paint() // permet de dessiner des formes
        var rect = RectF() // permet de dessiner un rectangle arrondi avec drawRoundRect a la position top, left, right, bottom
        var position = PointF() // permet de dessiner un rectangle arrondi a la position x,y
        private val cornerRadius = 10f // Rayon des coins en pixels
        private val rectWidth = 150f
        private val rectHeight = 100f
        private var rectangles = ArrayList<RectF>()
    */
    private var objet = Objet()
   // private var objets = ArrayList<Objet>()
    var mode = Mode.MOVE
    private var isDragging = false
    private val tempLineStart = PointF()
    private val tempLineEnd = PointF()
    private var graph = Graph()

    init {
        /* paint.color = Color.RED
         paint.style = Paint.Style.FILL*/
    }

    override fun onDraw(canvas: Canvas) {
        //Grace a cette fonction on peut dessiner sur la vue un rectangle arrondi a la position 100,100
        // Définir le rectangle arrondi à la position spécifiée
        // Rectangle arrondi
        //canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        // dessine tous les rectangles arrondis de la liste rectangles
/*        for (objet in objets) {
            canvas.drawRoundRect(objet.rect, Objet.cornerRadius, Objet.cornerRadius, Objet.paint)
        }*/
        if (graph.objets.isNotEmpty()){
            for (obj in graph.objets){
                canvas.drawRoundRect(obj.rect, Objet.cornerRadius, Objet.cornerRadius, Objet.paint)
            }
        }
        if (graph.connexion.isNotEmpty()){
            for (cnn in graph.connexion){
                canvas.drawLine(cnn.startConnexion.x, cnn.startConnexion.y, cnn.endConnexion.x, cnn.endConnexion.y, cnn.color)
            }
        }


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // dessiner en fonction du mode de dessin
        if (mode == Mode.ADD) {
            drawObject(event)
        } else if (mode == Mode.CONNECT) {
            drawConnexion(event)
        }
        /*      //desine un rectangle arrondi a la position du doigt quand on touche l'ecran dans la drawZone
              if (event != null) {
                  if (event.action == MotionEvent.ACTION_DOWN) {
                      val newObjet = Objet()
                      val newPositionx = event.x
                      val newPositiony = event.y
                      val newRect = RectF(newPositionx, newPositiony, newPositionx + Objet.rectWidth, newPositiony + Objet.rectHeight)
                      // rect.set(position.x, position.y, position.x + rectWidth, position.y + rectHeight)
                      newObjet.rect = newRect
                      objets.add(newObjet)
                      Log.d("Objets : = ", objets.toString())
                      invalidate()
                  }
              }*/
        return super.onTouchEvent(event)
    }

    private fun drawConnexion(event: MotionEvent?) {
       // TODO("a faire"
        val connexion = Connexion()
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    tempLineStart.x = event.x
                    tempLineStart.y = event.y
                    tempLineEnd.x = event.x
                    tempLineEnd.y = event.y
                    isDragging = true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        tempLineEnd.x = event.x
                        tempLineEnd.y = event.y
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (isDragging) {
                        tempLineEnd.x = event.x
                        tempLineEnd.y = event.y
                        isDragging = false
                        val cnn = Connexion()
             /*           cnn.startPoint = tempLineStart
                        cnn.endPoint = tempLineEnd
                        cnn.color = Objet.paint
                        cnn.labelColor = Objet.paint
                        cnn.label = "Connexion"*/
                        graph.connexion.add(cnn)
                        invalidate()
                    }
                }
            }
        }
    }

    private fun drawObject(event: MotionEvent?) {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val newObjet = Objet()
                val newPositionX = event.x
                val newPositionY = event.y
                newObjet.position = PointF(newPositionX, newPositionY)
                val newRect = RectF(newPositionX, newPositionY, newPositionX + Objet.rectWidth, newPositionY + Objet.rectHeight)
                newObjet.rect = newRect
                graph.objets.add(newObjet)
               // objets.add(newObjet)
                Log.d("Objets : = ", graph.objets.toString())
                invalidate()
            }
        }
    }

    // permet de vide la liste rectangles et de redessiner la vue
    /*    fun clearRectangles(){
            rectangles.clear()
            invalidate()
        }*/

}