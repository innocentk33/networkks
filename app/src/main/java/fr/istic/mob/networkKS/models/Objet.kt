package fr.istic.mob.networkKS.models

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import fr.istic.mob.networkKS.Connexion

class Objet {
    // description de companion object: https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects
    //Creer des objets statiques qui appartiennent a la class elle meme et non aux instance de la classe
    companion object {
        val paint = Paint() // permet de dessiner des formes
        const val cornerRadius = 10f // Rayon des coins en pixels
        const val rectWidth = 150f
        const val rectHeight = 100f
        const val labelTextSize =  30f
        const val labelPositionx =  10f
        const val labelPositiony =  1000f
    }

    var rect = RectF() // permet de dessiner un rectangle arrondi
    var position = PointF() // permet de dessiner un rectangle arrondi a la position x,y
    var objetColor = Paint()
    var objetStyle = Paint.Style.FILL
    var label = "Aucun nom"
    var connexions = ArrayList<Connexion>()
    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }
}