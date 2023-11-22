package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Region
import android.util.Log
import android.view.ContextMenu
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import com.google.gson.Gson
import fr.istic.mob.networkKS.models.Graph
import fr.istic.mob.networkKS.models.Objet


//ajouter un gestur detector pour detecter les gestes de l'utilisateur

class DrawZone(context: Context) : View(context), GestureDetector.OnGestureListener{
    private var objet = Objet(0f,0f,"")
    var mode = Mode.MOVE
    private var graph = Graph(ArrayList<Objet>(), ArrayList<Connexion>())
    private var startObject: Objet? = null
    private val tempPath = Path()
    private val gestureDetector = GestureDetectorCompat(context, this)
    private var isDragging = false
    private var isDraggingConnexion = false
    private var isCreatingConnection = false
    private var draggingObject = Objet(0f,0f,"")
    private var draggingConnexion = Connexion(0f,0f,0f,0f,Objet(0f,0f,""),Objet(0f,0f,""),"")
    private var findObjet = false
    private var findConnexion = false
    private var connexion = Connexion()

    override fun onDraw(canvas: Canvas) {
        if (graph.objets.isNotEmpty()){
            for (obj in graph.objets){
                canvas.drawRoundRect(obj.rect, Objet.cornerRadius, Objet.cornerRadius, obj.paint)
                // dessiner le label
                canvas.drawText(obj.label, obj.rect.centerX(), obj.position.y + Objet.labelPositionY, Objet.labelStyle)
            }
            // Dessiner les connexions existantes
            for (connexion in graph.connexions) {
                // Dessiner la connexion avec drawPath
                canvas.drawPath(connexion.path, connexion.connectionPaint)
                // centrer le label
               // val labelPositionX = (connexion.startConnexionX + connexion.endConnexionX) / 2
              //  val labelPositionY = (connexion.startConnexionY + connexion.endConnexionY) / 2
                canvas.drawText(connexion.connexionLabel, connexion.labelPositionX, connexion.labelPositionY, connexion.labelStyle)
            }
            if (isCreatingConnection) {
                // Dessinez la connexion temporaire
                canvas.drawPath(tempPath, Connexion.tempConnexionPaint)
            }

        }

    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (mode){
            Mode.ADD->{
                if (gestureDetector.onTouchEvent(event)) {
                    return true // si le geste est detecté on retourne true
                }
            }
            Mode.EDIT->{
               // findConnexionAtPoint(event.x,event.y)
                if (gestureDetector.onTouchEvent(event)) {
                    return true // si le geste est detecté on retourne true
                }
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
                        val touchedConnexion = findConnectionLabelAtPosition(event.x,event.y)
                        if (touchedObject != null) {
                            isDragging = true
                            findObjet = true
                            draggingObject = touchedObject
                        }
                       if (touchedConnexion != null){
                           isDraggingConnexion = true
                           findConnexion = true
                           draggingConnexion = touchedConnexion
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
                                draggingObject.position = newPositionAfterDrag
                                draggingObject.rect = RectF(
                                    newPositionAfterDrag.x,
                                    newPositionAfterDrag.y,
                                    newPositionAfterDrag.x + Objet.rectWidth,
                                    newPositionAfterDrag.y + Objet.rectHeight
                                )
                                for (connexion in graph.connexions) {
                                    if (connexion.startObjet == draggingObject) {
                                        connexion.startConnexionX = draggingObject.position.x + Objet.rectWidth / 2
                                        connexion.startConnexionY = draggingObject.position.y + Objet.rectHeight / 2
                                        // centrer le label
                                        connexion.labelPositionX = (connexion.startConnexionX + connexion.endConnexionX) / 2
                                        connexion.labelPositionY = (connexion.startConnexionY + connexion.endConnexionY) / 2
                                        connexion.labelPointF = PointF(connexion.labelPositionX, connexion.labelPositionY)
                                       connexion.path.apply {
                                             reset()
                                             moveTo(connexion.startConnexionX, connexion.startConnexionY)
                                             lineTo(connexion.endConnexionX, connexion.endConnexionY)
                                       }
                                    }
                                    if (connexion.endObjet == draggingObject) {

                                        connexion.endConnexionX = draggingObject.position.x + Objet.rectWidth / 2
                                        connexion.endConnexionY = draggingObject.position.y + Objet.rectHeight / 2
                                        // centrer le label
                                        connexion.labelPositionX = (connexion.startConnexionX + connexion.endConnexionX) / 2
                                        connexion.labelPositionY = (connexion.startConnexionY + connexion.endConnexionY) / 2
                                        connexion.labelPointF = PointF(connexion.labelPositionX, connexion.labelPositionY)
                                        connexion.path.apply {
                                            reset()
                                            moveTo(connexion.startConnexionX, connexion.startConnexionY)
                                            lineTo(connexion.endConnexionX, connexion.endConnexionY)
                                        }
                                    }
                                    graph.connexions[graph.connexions.indexOf(connexion)].path = connexion.path
                                    graph.connexions[graph.connexions.indexOf(connexion)].labelPositionX = connexion.labelPositionX
                                    graph.connexions[graph.connexions.indexOf(connexion)].labelPositionY = connexion.labelPositionY
                                    graph.connexions[graph.connexions.indexOf(connexion)].labelPointF = connexion.labelPointF
                                }
                                invalidate()
                            }
                        }
                        if (isDraggingConnexion){
                            if (findConnexion){
                                // utiliser Path.quadTo() pour dessiner une courbe de bézier
                                val newPositionAfterDrag = PointF(event.x, event.y)
                                draggingConnexion.labelPositionX = newPositionAfterDrag.x
                                draggingConnexion.labelPositionY = newPositionAfterDrag.y
                                draggingConnexion.labelPointF = PointF(draggingConnexion.labelPositionX, draggingConnexion.labelPositionY)
                                draggingConnexion.path.apply {
                                    reset()
                                    moveTo(draggingConnexion.startConnexionX, draggingConnexion.startConnexionY)
                                    quadTo(event.x, event.y, draggingConnexion.endConnexionX, draggingConnexion.endConnexionY)
                                }
                                graph.connexions[graph.connexions.indexOf(draggingConnexion)].path = draggingConnexion.path
                                graph.connexions[graph.connexions.indexOf(draggingConnexion)].labelPositionX = draggingConnexion.labelPositionX
                                graph.connexions[graph.connexions.indexOf(draggingConnexion)].labelPositionY = draggingConnexion.labelPositionY
                                graph.connexions[graph.connexions.indexOf(draggingConnexion)].labelPointF = draggingConnexion.labelPointF
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
                        isDragging = false
                        isDragging = false
                        findConnexion = false

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
            alertDialogConnexion.setPositiveButton(R.string.alerteDialog_confirm) { _, _ ->
                connexion = Connexion().createConnectionWithLabel(start,end,editTextConnexion.text.toString())
                graph.connexions.add(connexion)
                Log.d("Connexions",graph.connexions.toString())
                Log.d("Connexions",connexion.toString())
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

    private fun findConnectionLabelAtPosition(x: Float, y: Float): Connexion? {
        for (connexion in graph.connexions) {
            val xRange = connexion.labelPositionX - 50..connexion.labelPositionX + 50
            val yRange = connexion.labelPositionY - 50..connexion.labelPositionY + 50
            if (x in xRange && y in yRange) {
                return connexion
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

        val touchedObject = findObjectAtPoint(p0.x, p0.y)
        val touchedConnexionLabel = findConnectionLabelAtPosition(p0.x,p0.y)
        if (touchedObject != null) {
            editObjectMenu(touchedObject)
        }
        if (touchedConnexionLabel != null){
            editConnexionMenu(touchedConnexionLabel)
        }
     /*   if (touchedConnexion != null){
            editConnexionMenu(touchedConnexion)
        }*/
       if (mode == Mode.ADD){
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
        if (sharedPreferences != null) {
            val graphJson = sharedPreferences.getString("graph", null)
            if (graphJson != null) {
                val gson = Gson()
                val graphRestored = gson.fromJson(graphJson, Graph::class.java) // convertir le json en objet graph
                for (connexion in graphRestored.connexions) {
                    connexion.path = Path()
                    connexion.path.apply {
                        reset()
                        moveTo(connexion.startConnexionX, connexion.startConnexionY)
                        lineTo(connexion.endConnexionX, connexion.endConnexionY)
                    }
                }
                this.graph = graphRestored // mettre à jour le graph de la zone de dessin
                Log.d("Graph", graph.toString())
                invalidate() // redessiner la zone de dessin

        }
        } else {
            Toast.makeText(context, R.string.no_saved_network, Toast.LENGTH_SHORT).show()
        }

    }

    override fun createContextMenu(menu: ContextMenu?) {
        super.createContextMenu(menu)
    }

    override fun onCreateContextMenu(menu: ContextMenu?) {
        super.onCreateContextMenu(menu)
    }

    // creer un menu qui s'oouvre lorsqu'on clique longuement sur un objet
     private fun popupMenuObjet(objet: Objet,drawZone: DrawZone) {
        val popupMenu = PopupMenu(context, drawZone, Gravity.START)
        popupMenu.inflate(R.menu.context_menu_objet)
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.edit_object -> {
                    //afficher une boite de dialogue pour saisir le nom de l'objet
                    val alertDialog =
                        AlertDialog.Builder(context).setTitle(R.string.alerteDialog_title)
                            .setMessage(R.string.alerteDialog_message)
                    val editText = EditText(context)
                    alertDialog.setView(editText)
                    alertDialog.setPositiveButton(R.string.alerteDialog_confirm) { dialog, which ->
                        objet.label = editText.text.toString()
                        invalidate()
                    }
                    alertDialog.setNegativeButton(R.string.alerteDialog_cancel) { dialog, which ->
                        dialog.cancel()
                    }
                    alertDialog.show()
                    true
                }

                R.id.delete_object -> {
                    graph.objets.remove(objet)
                    invalidate()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()


    }



    // ouvrir une boite de dialogue pour modifier notre objet. les menus ne fonctionnent pas bien
    private fun editObjectMenu(objet: Objet) {
        // Charger le layout personnalisé de la boîte de dialogue
        val alertDialogLayout = LayoutInflater.from(context).inflate(R.layout.context_menu_layout, null)

        // Créer la boîte de dialogue
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(R.string.alerteDialog_edit_menu_title)
            .setView(alertDialogLayout) // Utilisez setView pour définir la vue


        // obtenir les references des boutons dans le layout de la boite de dialogue
        val editText = alertDialogLayout.findViewById<EditText>(R.id.editTextObjectName)
        val redColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonRed)
        val greenColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonGreen)
        val blueColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonBlue)
        val magentaColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonMagenta)
        val cyanColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonCyan)
        val orangeColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonYellow)
        val deleteObjet = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonDelete)
        editText.setText( graph.objets[graph.objets.indexOf(objet)].label)
        when (graph.objets[graph.objets.indexOf(objet)].color){
            Color.RED -> redColor.isChecked = true
            Color.GREEN -> greenColor.isChecked = true
            Color.BLUE -> blueColor.isChecked = true
            Color.MAGENTA -> magentaColor.isChecked = true
            Color.CYAN -> cyanColor.isChecked = true
            Color.YELLOW -> orangeColor.isChecked = true
        }
        alertDialog.setPositiveButton(R.string.alerteDialog_confirm) { _, _ ->
            objet.label = editText.text.toString()
            if (greenColor.isChecked){
                objet.color = Color.GREEN
                graph.objets[graph.objets.indexOf(objet)].color = objet.color
                objet.paint.color = objet.color
                graph.objets[graph.objets.indexOf(objet)].paint.color = objet.color

            }
            if (redColor.isChecked){
                objet.color = Color.RED
                graph.objets[graph.objets.indexOf(objet)].color = objet.color
                objet.paint.color = objet.color
                graph.objets[graph.objets.indexOf(objet)].paint.color = objet.color

            }
            if (blueColor.isChecked){
                objet.color = Color.BLUE
                graph.objets[graph.objets.indexOf(objet)].color = objet.color
                objet.paint.color = objet.color
                graph.objets[graph.objets.indexOf(objet)].paint.color = objet.color

            }
            if (magentaColor.isChecked){
                objet.color = Color.MAGENTA
                graph.objets[graph.objets.indexOf(objet)].color = objet.color
                objet.paint.color = objet.color
                graph.objets[graph.objets.indexOf(objet)].paint.color = objet.color

            }
            if (cyanColor.isChecked){
                objet.color = Color.CYAN
                graph.objets[graph.objets.indexOf(objet)].color = objet.color
                objet.paint.color = objet.color
                graph.objets[graph.objets.indexOf(objet)].paint.color = objet.color

            }
            if (orangeColor.isChecked){
                objet.color = Color.YELLOW
                graph.objets[graph.objets.indexOf(objet)].color = objet.color
                objet.paint.color = objet.color
                graph.objets[graph.objets.indexOf(objet)].paint.color = objet.color

            }

            if (deleteObjet.isChecked){
                graph.objets.remove(objet)
                graph.connexions.removeIf { it.startObjet == objet || it.endObjet == objet }
            }


            invalidate()
        }
        alertDialog.setNegativeButton(R.string.alerteDialog_cancel) { dialog, which ->
            dialog.cancel()
        }
        // Afficher la boîte de dialogue
        alertDialog.show()
    }


    private fun editConnexionMenu(connexion: Connexion) {
        // Charger le layout personnalisé de la boîte de dialogue
        val alertDialogLayout = LayoutInflater.from(context).inflate(R.layout.edit_connexion_layout, null)

        // Créer la boîte de dialogue
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(R.string.alertDialog_connexion_edit_title)
            .setView(alertDialogLayout) // Utilisez setView pour définir la vue


        // obtenir les references des boutons dans le layout de la boite de dialogue
        val editText = alertDialogLayout.findViewById<EditText>(R.id.editTextObjectName)
        val redColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonRed)
        val greenColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonGreen)
        val blueColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonBlue)
        val magentaColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonMagenta)
        val yellowColor = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonYellow)
        val deleteConnexion = alertDialogLayout.findViewById<RadioButton>(R.id.radioButtonDelete)
        val small = alertDialogLayout.findViewById<RadioButton>(R.id.radioConnexionTaille_Petite)
        val medium =alertDialogLayout.findViewById<RadioButton>(R.id.radioConnexionTaille_Moyenne)
        val large = alertDialogLayout.findViewById<RadioButton>(R.id.radioConnexionTaille_Grande)

        editText.setText( graph.connexions[graph.connexions.indexOf(connexion)].connexionLabel)
        when (graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.color){
            Color.RED-> redColor.isChecked = true
            Color.GREEN-> greenColor.isChecked = true
            Color.BLUE -> blueColor.isChecked = true
            Color.MAGENTA -> magentaColor.isChecked = true
            Color.YELLOW -> yellowColor.isChecked = true
        }
        alertDialog.setPositiveButton(R.string.alerteDialog_confirm) { _, _ ->
            connexion.connexionLabel = editText.text.toString()

            if (greenColor.isChecked){
                connexion.connectionPaint.color = Color.GREEN
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.color = Color.GREEN
            }
            if (redColor.isChecked){
                connexion.connectionPaint.color = Color.RED
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.color = Color.RED
            }
            if (blueColor.isChecked){
                connexion.connectionPaint.color = Color.BLUE
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.color = Color.BLUE
            }
            if (magentaColor.isChecked){
                connexion.connectionPaint.color = Color.MAGENTA
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.color = Color.MAGENTA
            }
            if (yellowColor.isChecked){
                connexion.connectionPaint.color = Color.YELLOW
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.color = Color.YELLOW
            }
            if (small.isChecked){
                connexion.connectionPaint.strokeWidth = Connexion.smallStroke
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.strokeWidth = Connexion.smallStroke
            }
            if (medium.isChecked){
                connexion.connectionPaint.strokeWidth = Connexion.mediumStroke
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.strokeWidth = Connexion.mediumStroke
            }
            if (large.isChecked){
                connexion.connectionPaint.strokeWidth = Connexion.largeStroke
                graph.connexions[graph.connexions.indexOf(connexion)].connectionPaint.strokeWidth = Connexion.largeStroke
            }

            if (deleteConnexion.isChecked){
                graph.connexions.remove(connexion)
            }


            invalidate()
        }
        alertDialog.setNegativeButton(R.string.alerteDialog_cancel) { dialog, which ->
            dialog.cancel()
        }
        // Afficher la boîte de dialogue
        alertDialog.show()
    }


}