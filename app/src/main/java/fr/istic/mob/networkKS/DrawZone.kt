package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
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
    private var objet = Objet()
    var mode = Mode.MOVE
    private var isDragging = false
    private val tempLineStart = PointF()
    private val tempLineEnd = PointF()
    private var graph = Graph()
    private var connectionMode = false
    private var startObject: Objet? = null
    private var endObject: Objet? = null
    private val tempPath = Path()
    //private val gestureDetector = GestureDetectorCompat()
    private val gestureDetector = GestureDetectorCompat(context, this)
    private val connectionPaint = Paint().apply {
        color = android.graphics.Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    init {


    }

    override fun onDraw(canvas: Canvas) {
        if (graph.objets.isNotEmpty()){
            for (obj in graph.objets){
                canvas.drawRoundRect(obj.rect, Objet.cornerRadius, Objet.cornerRadius, Objet.paint)
                // dessiner le label
                canvas.drawText(obj.label, obj.rect.centerX(), obj.position.y + Objet.labelPositionY, Objet.labelStyle)
            }
            // Dessiner les connexions existantes
            for (connexion in graph.connexions) {
                canvas.drawLine(
                    connexion.startConnexion.x, connexion.startConnexion.y,
                    connexion.endConnexion.x, connexion.endConnexion.y,
                    connectionPaint
                )
            }
            // Dessiner la connexion temporaire
            if (connectionMode) {
                canvas.drawLine(
                    tempLineStart.x, tempLineStart.y,
                    tempLineEnd.x, tempLineEnd.y,
                    connectionPaint
                )
            }
        }

    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(event)) {
            return true // si le geste est detecté on retourne true
        }else {
                  if(event.action == MotionEvent.ACTION_DOWN){
                      Log.d("OnTouchEvent","OnTouchEvent")

                  }
            return  false
        }


    }

    private fun drawConnexion(event: MotionEvent?) {
       // TODO("a faire"

    }

/*     fun createConnection(start: Objet, end: Objet) {
        // Créez une connexion entre les objets avec une ligne droite
        val connexion = Connexion()
        connexion.startObjet = start
        connexion.endObjet = end
        // Calculez les coordonnées de début et de fin de la ligne droite
        connexion.startConnexion = PointF(start.position.x + Objet.rectWidth / 2, start.position.y + Objet.rectHeight / 2)
        connexion.endConnexion = PointF(end.position.x + Objet.rectWidth / 2, end.position.y + Objet.rectHeight / 2)
        // Ajoutez la connexion à la liste des connexions de votre modèle
        graph.connexions.add(connexion)
    }*/

    private fun createConnection(start: Objet?, end: Objet?) {
        if (start != null && end != null) {
            val connection = Objet.createConnection(start, end)
            graph.connexions.add(connection)
            invalidate()
        }
    }

    private fun findObjectAtPoint(x: Float, y: Float): Objet? {
        for (objet in graph.objets) {
            if (objet.rect.contains(x, y)) {
                return objet
            }
        }
        return null
    }

    override fun onDown(p0: MotionEvent): Boolean {
       // Log.d("OnDown","OnDown")
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
        //TODO("Not yet implemented")
      //  Log.d("OnShowPress","OnShowPress")
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        //TODO("Not yet implemented")
        //Log.d("OnSingleTapUp","OnSingleTapUp")
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
       // TODO("Not yet implemented")
       // Log.d("OnScroll","OnScroll")
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
        //Log.d("OnFling","OnFling")
        return true
    }


}