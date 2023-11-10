package fr.istic.mob.networkKS

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import com.google.gson.annotations.SerializedName
import fr.istic.mob.networkKS.models.Objet
import java.io.Serializable

data class Connexion(
    @SerializedName("startConnexionX")
    var startConnexionX: Float = 0f,
    @SerializedName("startConnexionY")
    var startConnexionY: Float = 0f,
    @SerializedName("endConnexionX")
    var endConnexionX: Float = 0f,
    @SerializedName("endConnexionY")
    var endConnexionY: Float = 0f,
    @SerializedName("startObjet")
    var startObjet: Objet = Objet(0f, 0f, ""),
    @SerializedName("endObjet")
    var endObjet: Objet = Objet(0f, 0f, ""),
    @SerializedName("connexionLabel")
    var connexionLabel: String = "",
    @SerializedName("labelPositionX")
    var labelPositionX: Float = 0f,
    @SerializedName("labelPositionY")
    var labelPositionY: Float = 0f,



    ) : Serializable {
    companion object {
        val tempConnexionPaint = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }
        const val smallStroke = 10f
        const val mediumStroke = 20f
        const val largeStroke = 30f
    }

    var connectionPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    var path = Path()
    var labelPointF = PointF()
    val labelRectF = RectF()
    var labelStyle = Paint().apply {
        Paint.Style.FILL
        Color.BLACK
        textSize = 30f
        typeface = android.graphics.Typeface.DEFAULT_BOLD
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    init {

    }

    fun createConnectionWithLabel(start: Objet, endObjet: Objet, label: String): Connexion {
        // Créez une connexion entre les objets avec une ligne droite
        val connexion = Connexion()
        connexion.startObjet = start
        connexion.endObjet = endObjet
        connexion.startConnexionX = start.position.x + Objet.rectWidth / 2
        connexion.startConnexionY = start.position.y + Objet.rectHeight / 2
        connexion.endConnexionX = endObjet.position.x + Objet.rectWidth / 2
        connexion.endConnexionY = endObjet.position.y + Objet.rectHeight / 2
        connexion.connexionLabel = label


        connexion.path = Path().apply {
            moveTo(connexion.startConnexionX, connexion.startConnexionY)
            lineTo(connexion.endConnexionX, connexion.endConnexionY)
        }


        // centrer le label
        connexion.labelPositionX = (connexion.startConnexionX + connexion.endConnexionX) / 2
        connexion.labelPositionY = (connexion.startConnexionY + connexion.endConnexionY) / 2
        connexion.labelPointF = PointF(connexion.labelPositionX, connexion.labelPositionY)
        return connexion

    }
}