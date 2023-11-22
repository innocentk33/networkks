package fr.istic.mob.networkKS.utils

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Capture {

    fun Screenshot() {
        // Obtenir la vue racine de votre activité

        val rootView = view.decorView.rootView

        // Créer un Bitmap de la vue
        val screenshotBitmap = Bitmap.createBitmap(
            rootView.width,
            rootView.height,
            Bitmap.Config.ARGB_8888
        )

        // Créer un canvas avec le Bitmap
        val canvas = Canvas(screenshotBitmap)
        rootView.draw(canvas)

        // Enregistrer le Bitmap dans un fichier
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val screenshotFile = File.createTempFile(
            "screenshot",
            ".png",
            storageDir
        )

        try {
            val fileOutputStream = FileOutputStream(screenshotFile)
            screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}