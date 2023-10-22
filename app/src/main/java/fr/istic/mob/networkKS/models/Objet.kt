package fr.istic.mob.networkKS.models

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent
import fr.istic.mob.networkKS.Connexion
import fr.istic.mob.networkKS.DrawZone

class Objet {
    // description de companion object: https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects
    //il permet de Creer des objets statiques qui appartiennent a la class elle meme et non aux instance de la classe
    companion object {

        val paint = Paint() // permet de dessiner des formes
        const val cornerRadius = 10f // Rayon des coins en pixels
        const val rectWidth = 150f
        const val rectHeight = 100f
        //centrer le label

        const val labelPositionX =  rectWidth+30f
        const val labelPositionY =  rectHeight+30f
        var labelStyle = Paint()

    }

    var rect = RectF() // permet de dessiner un rectangle arrondi
    var position = PointF() // permet de dessiner un rectangle arrondi a la position x,y
    var label = "Aucun nom"
    var connexions = ArrayList<Connexion>()
    var drawZone : DrawZone? = null

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        labelStyle.style = Paint.Style.FILL
        labelStyle.color = Color.BLACK
        labelStyle.textSize = 30f
        labelStyle.typeface = android.graphics.Typeface.DEFAULT_BOLD
        labelStyle.isAntiAlias = true
        labelStyle.textAlign = Paint.Align.CENTER
    }
    //dessiner un objet sur la vue
    fun drawObjet(){
        // Définir le rectangle arrondi à la position spécifiée
        // Rectangle arrondi
        rect = RectF(position.x, position.y, position.x + rectWidth, position.y + rectHeight)
        // dessine tous les rectangles arrondis de la liste rectangles
        drawZone?.invalidate()
    }

    fun createObjetAtPosition(event: MotionEvent?) :Objet{
            val newObjet = Objet()
        if (event != null) {
                val newPositionX = event.x
                val newPositionY = event.y
                newObjet.position = PointF(newPositionX, newPositionY)
                val newRect = RectF(newPositionX, newPositionY, newPositionX + Objet.rectWidth, newPositionY + Objet.rectHeight)
                newObjet.rect = newRect
        }
            return newObjet
    }
    fun createObjetAtPositionWithLabel(event: MotionEvent?,label :String) :Objet{
        val newObjet = Objet()
        if (event != null) {
            val newPositionX = event.x
            val newPositionY = event.y
            newObjet.position = PointF(newPositionX, newPositionY)
            val newRect = RectF(newPositionX, newPositionY, newPositionX + Objet.rectWidth, newPositionY + Objet.rectHeight)
            newObjet.rect = newRect
            newObjet.label = label
        }
        return newObjet
    }


}