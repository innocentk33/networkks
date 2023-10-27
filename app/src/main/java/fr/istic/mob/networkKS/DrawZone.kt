package fr.istic.mob.networkKS

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF
import android.net.Uri
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import com.google.gson.Gson
import fr.istic.mob.networkKS.models.Graph
import fr.istic.mob.networkKS.models.Objet
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.log

//ajouter un gestur detector pour detecter les gestes de l'utilisateur

class DrawZone(context: Context) : View(context), GestureDetector.OnGestureListener{
    private var objet = Objet(0f,0f,"")
    var mode = Mode.MOVE
    var graph = Graph(ArrayList<Objet>(), ArrayList<Connexion>())
    private var startObject: Objet? = null
    private val tempPath = Path()
    private val gestureDetector = GestureDetectorCompat(context, this)
    private var isDragging = false
    private var isCreatingConnection = false
    private var draggingObject = Objet(0f,0f,"")
    private var findObjet = false
    private var connexion = Connexion()
    private var toolbarHeight = 100f
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

                    connexion.startConnexionX, connexion.startConnexionY,
                   connexion.endConnexionX, connexion.endConnexionY,
                    connexion.connectionPaint
                )
                // Dessiner le label centrer
                val labelPositionX = (connexion.startConnexionX + connexion.endConnexionX) / 2
                val labelPositionY = (connexion.startConnexionY + connexion.endConnexionY) / 2
                canvas.drawText(connexion.connexionLabel, labelPositionX, labelPositionY, Objet.labelStyle)
                //canvas.drawText()
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
               // Log.d("GraphConnexion", graph.toString())

            }
            Mode.EDIT->{

            }
            Mode.CONNECT->{
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("Graph", graph.toString())
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
                        if (isCreatingConnection) {
                            // ici je met à jour le chemin temporaire à chaque mouvement du doigt
                            tempPath.lineTo(event.x, event.y)
                            invalidate()
                        }
                        return true

                    }
                    MotionEvent.ACTION_UP -> {
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
                        val drawZoneHeight = height.toFloat() // la hauteur de la zone de dessin - la hauteur de la toolbar
                        val drawZoneWidth = width.toFloat()
                        if (isDragging) {
                            if (findObjet) {
                                val newPositionAfterDrag = PointF(event.x, event.y)
                                //limiter la zone de deplacement des objets dans le frameLayout
                                if (newPositionAfterDrag.y <0) {
                                    newPositionAfterDrag.y = 0f
                                }
                                //limiter la zone de deplacement des objets dans le frameLayout petite condition magique
                                if (newPositionAfterDrag.y >= (drawZoneHeight - Objet.rectHeight - (Objet.labelPositionY - Objet.rectHeight))){
                                    newPositionAfterDrag.y = drawZoneHeight - Objet.rectHeight - (Objet.labelPositionY - Objet.rectHeight)
                                }
                                //deuxieme petite condition magique
                                if (newPositionAfterDrag.x >= (drawZoneWidth - Objet.rectWidth)){
                                    newPositionAfterDrag.x = drawZoneWidth - Objet.rectWidth
                                }
                                if (newPositionAfterDrag.x <0){
                                    newPositionAfterDrag.x = 0f
                                }
                                Log.d("INNOCENT : X", newPositionAfterDrag.x.toString())
                                Log.d("INNOCENT : drawZoneHeight", drawZoneWidth.toString())
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
                                       // connexion.startConnexion.offset(offsetX, offsetY)
                                        connexion.startConnexionX = connexion.startConnexionX + offsetX
                                        connexion.startConnexionY = connexion.startConnexionY + offsetY
                                    }
                                    if (connexion.endObjet == draggingObject) {
                                       // connexion.endConnexion.offset(offsetX, offsetY)
                                        connexion.endConnexionX = connexion.endConnexionX + offsetX
                                        connexion.endConnexionY = connexion.endConnexionY + offsetY
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
            // afficher une boite de dialogue pour saisir le nom de la connexion
            val alertDialogConnexion = AlertDialog.Builder(context).setTitle(R.string.alerteDialog_connexion_title).setMessage(R.string.alerteDialog_connexion_message)
            val editTextConnexion = EditText(context)
            alertDialogConnexion.setView(editTextConnexion)
            alertDialogConnexion.setPositiveButton(R.string.alerteDialog_confirm) { dialog, which ->
                connexion = connexion.createConnectionWithLabel(start,end,editTextConnexion.text.toString())
                graph.connexions.add(connexion)
                invalidate()
            }
            alertDialogConnexion.setNegativeButton(R.string.alerteDialog_cancel) { dialog, which ->
                dialog.cancel()
            }
            alertDialogConnexion.show()
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
        Log.d("OnLongPress","Appui long détecté")
        //afficher une boite de dialogue pour saisir le nom de l'objet
        val alertDialog = AlertDialog.Builder(context).setTitle(R.string.alerteDialog_title).setMessage(R.string.alerteDialog_message)
        val editText = EditText(context)
        alertDialog.setView(editText)
        alertDialog.setPositiveButton(R.string.alerteDialog_confirm) { dialog, which ->
            objet = objet.createObjetAtPositionWithLabel(p0,editText.text.toString())
            graph.objets.add(objet)
            invalidate()
        }
        alertDialog.setNegativeButton(R.string.alerteDialog_cancel) { dialog, which ->
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

    fun saveGraph() {

        if (graph.objets.isNotEmpty()) {
            Log.d("Graph", graph.toString())
            val gson = Gson()
            val graphJson = gson.toJson(graph)
            Log.d("GraphJson", graphJson)
            val sharedPreferences = context.getSharedPreferences("graph", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("graph", graphJson)
            editor.apply()
            Toast.makeText(context, "Votre reseau a bien été enregistré", Toast.LENGTH_SHORT).show()
        }
    }

    fun viewSavedNetwork() {
        val sharedPreferences = context.getSharedPreferences("graph", Context.MODE_PRIVATE)
        val graphJson = sharedPreferences.getString("graph", null)
        if (graphJson != null) {
            val gson = Gson()
            val graph = gson.fromJson(graphJson, Graph::class.java) // convertir le json en objet graph
            this.graph = graph // mettre à jour le graph de la zone de dessin
            invalidate() // redessiner la zone de dessin
        }
    }




}