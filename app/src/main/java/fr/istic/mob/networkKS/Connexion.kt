package fr.istic.mob.networkKS

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import com.google.gson.annotations.SerializedName
import fr.istic.mob.networkKS.models.Objet
import java.io.Serializable

data class Connexion (
    @SerializedName("startConnexionX")
    var startConnexionX: Float = 0f,
    @SerializedName("startConnexionY")
    var startConnexionY: Float = 0f,
    @SerializedName("endConnexionX")
    var endConnexionX: Float = 0f,
    @SerializedName("endConnexionY")
    var endConnexionY: Float = 0f,
    @SerializedName("startObjet")
    var startObjet :Objet = Objet(0f,0f,""),
    @SerializedName("endObjet")
    var endObjet :Objet = Objet(0f,0f,"")

) :Serializable{
    companion object{
        val tempConnexionPaint = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }
    }
    private var color = Paint()


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
       // connexion.startConnexion = PointF(start.position.x + Objet.rectWidth / 2, start.position.y + Objet.rectHeight / 2)
        connexion.startConnexionX = start.position.x + Objet.rectWidth / 2
        connexion.startConnexionY = start.position.y + Objet.rectHeight / 2

       // connexion.endConnexion = PointF(end.position.x + Objet.rectWidth / 2, end.position.y + Objet.rectHeight / 2)
        connexion.endConnexionX = end.position.x + Objet.rectWidth / 2
        connexion.endConnexionY = end.position.y + Objet.rectHeight / 2

        return connexion
    }
}