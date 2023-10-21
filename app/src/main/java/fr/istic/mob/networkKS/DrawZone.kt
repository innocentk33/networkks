package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import fr.istic.mob.networkKS.models.Graph
import fr.istic.mob.networkKS.models.Objet
import kotlin.math.log

//ajouter un gestur detector pour detecter les gestes de l'utilisateur

class DrawZone(context: Context) : View(context), GestureDetector.OnGestureListener{
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

    //private val gestureDetector = GestureDetectorCompat()
    private val gestureDetector = GestureDetectorCompat(context, this)

    init {


    }

    override fun onDraw(canvas: Canvas) {
        if (graph.objets.isNotEmpty()){
            for (obj in graph.objets){
                canvas.drawRoundRect(obj.rect, Objet.cornerRadius, Objet.cornerRadius, Objet.paint)
                // dessiner le label
                canvas.drawText(obj.label, obj.rect.centerX(), obj.position.y + Objet.labelPositionY, Objet.labelStyle)
            }
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {


        if (gestureDetector.onTouchEvent(event)) {
            return true // L'appui a été géré par le GestureDetector
        }

        return false
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

    override fun onDown(p0: MotionEvent): Boolean {
        Log.d("OnDown","OnDown")
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
        //TODO("Not yet implemented")
        Log.d("OnShowPress","OnShowPress")
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        //TODO("Not yet implemented")
        Log.d("OnSingleTapUp","OnSingleTapUp")
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
       // TODO("Not yet implemented")
        Log.d("OnScroll","OnScroll")
        return true
    }

    override fun onLongPress(p0: MotionEvent) {
        Toast.makeText(context, "Appui long détecté", Toast.LENGTH_SHORT).show()
        Log.d("OnLongPress","OnLongPress")
        //afficher une boite de dialogue pour saisir le nom de l'objet
        val alertDialog = AlertDialog.Builder(context).setTitle("Nom de l'objet").setMessage("Saisir le nom de l'objet")
        val editText = EditText(context)
        alertDialog.setView(editText)
        alertDialog.setPositiveButton("Créer") { dialog, which ->
            objet = objet.createObjetAtPositionWithLabel(p0,editText.text.toString())
            objet.drawZone = this
            graph.objets.add(objet)
            invalidate()
        }
        alertDialog.setNegativeButton("Annuler") { dialog, which ->
            dialog.cancel()
        }
        alertDialog.show()
        Log.d("Objets : = ", graph.objets.toString())
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
       // TODO("Not yet implemented")
        Log.d("OnFling","OnFling")
        return true
    }


    // permet de vide la liste rectangles et de redessiner la vue
    /*    fun clearRectangles(){
            rectangles.clear()
            invalidate()
        }*/




}