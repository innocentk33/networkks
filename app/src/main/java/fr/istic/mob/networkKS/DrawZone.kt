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
    private var graph = Graph()
    private var connectionMode = false
    private var startObject: Objet? = null
    private var endObject: Objet? = null
    private val tempPath = Path()
    private val gestureDetector = GestureDetectorCompat(context, this)
    private var isDragging = false
    private var isCreatingConnection = false
    private var draggingObject = Objet()
    private var findObjet = false
    private val connectionPaint = Paint().apply {
        color = android.graphics.Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private var connexion = Connexion()
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
                    connexion.connectionPaint
                )
            }
            if (isCreatingConnection) {
                // Dessinez la connexion temporaire
                canvas.drawPath(tempPath, Connexion.tempConnexionPaint)
            }

        }

    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Faire un when sur le mode
        when (mode){
            Mode.ADD->{
                if (gestureDetector.onTouchEvent(event)) {
                    return true // si le geste est detecté on retourne true
                }
            }
            Mode.EDIT->{

            }
            Mode.CONNECT->{
            Log.d("mode connexion",mode.name.toString())
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("action","action down")
                        //verifier si l'objet est selectionné
                        val touchedObject = findObjectAtPoint(event.x, event.y)
                        if (touchedObject !=null){
                            isCreatingConnection = true
                            startObject = touchedObject
                            tempPath.reset()
                            tempPath.moveTo(touchedObject.rect.centerX(), touchedObject.rect.centerY())
                        }
                        return true

                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.d("action","action move")
                        if (isCreatingConnection) {
                            // Mettez à jour le chemin temporaire à chaque mouvement du doigt
                            tempPath.lineTo(event.x, event.y)
                            invalidate()
                        }
                        return true

                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("action","action up")
                        if (isCreatingConnection) {
                            // Vérifiez si le doigt est relâché sur un autre objet
                            val endObject = findObjectAtPoint(event.x, event.y)
                            if (endObject != null && endObject != startObject) {
                                // Créez la connexion entre les deux objets
                                createConnection(startObject, endObject)
                            }
                            isCreatingConnection = false
                            tempPath.reset()
                            invalidate()
                        }
                        return true

                    }
                }


            }
            Mode.MOVE->{
                when (event.action) {

                    MotionEvent.ACTION_DOWN -> {
                        val touchedObject = findObjectAtPoint(event.x, event.y)
                        if (touchedObject != null) {
                            isDragging = true
                            findObjet = true
                            draggingObject = touchedObject
                        }
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (isDragging) {
                            if (findObjet) {
                                val newPositionAfterDrag = PointF(event.x, event.y)
                                val offsetX = newPositionAfterDrag.x - draggingObject.position.x
                                val offsetY = newPositionAfterDrag.y - draggingObject.position.y
                                draggingObject.position = newPositionAfterDrag
                                draggingObject.rect = RectF(
                                    newPositionAfterDrag.x,
                                    newPositionAfterDrag.y,
                                    newPositionAfterDrag.x + Objet.rectWidth,
                                    newPositionAfterDrag.y + Objet.rectHeight
                                )
                                for (connexion in graph.connexions) {
                                    if (connexion.startObjet == draggingObject) {
                                        connexion.startConnexion.offset(offsetX, offsetY)
                                    }
                                    if (connexion.endObjet == draggingObject) {
                                        connexion.endConnexion.offset(offsetX, offsetY)
                                    }
                                }
                                invalidate()
                            }
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        isDragging = false
                       // draggingObject = null
                        findObjet = false
                        startObject = null
                        return true
                    }
                }
            }

        }
        return super.onTouchEvent(event)
    }

    private fun createConnection(start: Objet?, end: Objet?) {
        if (start != null && end != null) {
            val connection = connexion.createConnection(start,end)
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


}