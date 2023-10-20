package fr.istic.mob.networkKS

import android.graphics.Paint
import android.graphics.PointF
import fr.istic.mob.networkKS.models.Objet

class Connexion {
    var startConnexion = PointF()
    var endConnexion = PointF()
    var color = Paint()
    var startObjet = Objet()
    var endObjet = Objet()

    init {
        color = Paint()
        color.color = android.graphics.Color.BLUE
    }
}