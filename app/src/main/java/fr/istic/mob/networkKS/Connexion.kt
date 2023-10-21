package fr.istic.mob.networkKS

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import fr.istic.mob.networkKS.models.Objet

class Connexion {
    var startConnexion = PointF()
    var endConnexion = PointF()
    var color = Paint()
    var startObjet = Objet()
    var endObjet = Objet()
    var path = Path()
    init {
        color = Paint()
        color.color = Color.BLUE
    }
}