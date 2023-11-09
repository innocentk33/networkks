package fr.istic.mob.networkKS.models

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Objet(
    @SerializedName("positionX")
    var positionX: Float,

    @SerializedName("positionY")
    var positionY: Float,

    @SerializedName("label")
    var label: String,

/*    @SerializedName("connexions")
    var connexions: ArrayList<Connexion>*/
):Serializable{
    companion object {


        const val cornerRadius = 10f // Rayon des coins en pixels
        const val rectWidth = 150f
        const val rectHeight = 100f
        //centrer le label

        const val labelPositionX =  rectWidth+30f
        const val labelPositionY =  rectHeight+30f
        var labelStyle = Paint()

    }
    var color = Color.RED
    var rect = RectF() // permet de dessiner un rectangle arrondi
    var position = PointF() // permet de dessiner un rectangle arrondi a la position x,y
    var paint = Paint() // permet de dessiner des formes

    init {
        paint.color = color
        paint.style = Paint.Style.FILL
        labelStyle.style = Paint.Style.FILL
        labelStyle.color = Color.BLACK
        labelStyle.textSize = 30f
        labelStyle.typeface = android.graphics.Typeface.DEFAULT_BOLD
        labelStyle.isAntiAlias = true
        labelStyle.textAlign = Paint.Align.CENTER

    }
    fun createObjetAtPositionWithLabel(event: MotionEvent,label :String) :Objet{
        val newObjet = Objet(positionX = event.x, positionY = event.y, label = label) // Créez un objet à la position x,y
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