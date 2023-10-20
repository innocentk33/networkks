package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.istic.mob.networkKS.models.Graph
import fr.istic.mob.networkKS.models.Objet

class DrawZone(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
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
                // dessiner le label
                canvas.drawText(obj.label, obj.rect.centerX(), obj.position.y + Objet.labelPositionY, Objet.labelStyle)
            }
        }

    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // dessiner en fonction du mode de dessin
        if (mode == Mode.ADD) {
            graph.objets.add(objet.createObjetAtPosition(event))

        } else if (mode == Mode.CONNECT) {
            drawConnexion(event)
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    private fun drawConnexion(event: MotionEvent?) {
       // TODO("a faire"

    }

/*    private fun drawObject(event: MotionEvent?) {
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
    }*/
     fun createConnection(start: Objet, end: Objet) {
        // Créez une connexion entre les objets avec une ligne droite
        val connexion = Connexion()
        connexion.startObjet = start
        connexion.endObjet = end
        // Calculez les coordonnées de début et de fin de la ligne droite
        connexion.startConnexion = PointF(start.position.x + Objet.rectWidth / 2, start.position.y + Objet.rectHeight / 2)
        connexion.endConnexion = PointF(end.position.x + Objet.rectWidth / 2, end.position.y + Objet.rectHeight / 2)
        // Ajoutez la connexion à la liste des connexions de votre modèle
        graph.connexions.add(connexion)
    }
     fun findObjectAtPoint(x: Float, y: Float): Objet? {
        // Parcourez la liste d'objets et renvoyez l'objet qui contient les coordonnées (x, y)
        for (objet in graph.objets) {
            if (objet.rect.contains(x, y)) {
                return objet
            }
        }
        return null
    }



    // permet de vide la liste rectangles et de redessiner la vue
    /*    fun clearRectangles(){
            rectangles.clear()
            invalidate()
        }*/

}