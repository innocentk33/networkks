package fr.istic.mob.networkKS.models

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF

class Objet {
    // description de companion object: https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects
    //Creer des objets statiques qui appartiennent a la class elle meme et non aux instance de la classe
    companion object {
        val paint = Paint() // permet de dessiner des formes
        val cornerRadius = 10f // Rayon des coins en pixels
        val rectWidth = 150f
        val rectHeight = 100f
    }

    var rect = RectF() // permet de dessiner un rectangle arrondi
    var position = PointF() // permet de dessiner un rectangle arrondi a la position x,y

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }
}