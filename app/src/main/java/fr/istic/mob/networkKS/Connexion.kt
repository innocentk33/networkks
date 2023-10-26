package fr.istic.mob.networkKS

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import fr.istic.mob.networkKS.models.Objet

class Connexion {
    companion object{
        val tempConnexionPaint = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }
    }
    var startConnexion = PointF()
    var endConnexion = PointF()
    var color = Paint()
    var startObjet = Objet()
    var endObjet = Objet()
    var path = Path()
    var connectionPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    init {
        color = Paint()
        color.color = Color.BLUE
    }


    fun createConnection(start: Objet, end: Objet): Connexion {
        // Créez une connexion entre les objets avec une ligne droite
        val connexion = Connexion()
        connexion.startObjet = start
        connexion.endObjet = end
        // Calculez les coordonnées de début et de fin de la ligne droite
        connexion.startConnexion = PointF(start.position.x + Objet.rectWidth / 2, start.position.y + Objet.rectHeight / 2)
        connexion.endConnexion = PointF(end.position.x + Objet.rectWidth / 2, end.position.y + Objet.rectHeight / 2)
        return connexion
    }
}