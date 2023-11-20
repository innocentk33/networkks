package fr.istic.mob.networkKS.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.FileProvider
import fr.istic.mob.networkKS.DrawZone
import java.io.File
import java.io.FileOutputStream

class Utils {
    //envoyer un mail simple
    fun sendMail (view: View){

     /*   val fileDir = context.filesDir

        Log.d("File Dir: ",fileDir.absolutePath)
        val cacheDir = context.cacheDir
        Log.d("Cache File Dir: ",cacheDir.absolutePath)

*/

        try {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            val cacheDir = view.context.cacheDir
            // creer un fichier temporaire
            val tempImage = File.createTempFile("tempfile", ".png", cacheDir)
            val stream = FileOutputStream(tempImage)
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream)
            stream.flush()
            stream.close()

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "image/*"
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Capture d'écran")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Voici la capture d'écran de mon réseau")
            //val uri = Uri.fromFile(imagePath)
            //Log.d("Error Ex URI: ", uri.toString())
           // emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
           // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempImage))
            view.context.startActivity(
                Intent.createChooser(
                    emailIntent,
                    "Choisissez une application"
                )
            )


        } catch (e: Exception) {
            Log.d("Error Ex: ", e.message.toString())
            e.printStackTrace()
        }
    }

    // envoyer un mail avec une capture d'écran
    fun saveView(view: View){
try {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    val file = File(view.context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "reseau.jpg")
    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
    out.flush()
    out.close()

    val contentUri = FileProvider.getUriForFile(view.context, "${view.context.packageName}.fileprovider", file) // fr.istic.mob.networkKS.fileprovider

    // envoyer le mail
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.type = "image/*"
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Capture d'écran")
    emailIntent.putExtra(Intent.EXTRA_TEXT, "Voici la capture d'écran de mon réseau")
    emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    view.context.startActivity(
        Intent.createChooser(
            emailIntent,
            "Choisissez une application"
        )
    )
}catch (e:Exception){
e.printStackTrace()
    } }

    fun saveScreenshot(view :View,context:Context){
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        if (Utils().createScreenshotDirectory()) {
            val externalStorage = Environment.getExternalStorageDirectory()
            val dir = File(externalStorage.absolutePath, "graph")
    }
    }



    // demander l'autorisation d'acces au stockage externe
    fun askPermission(context: Context){
        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        val grant = context.checkCallingOrSelfPermission(permission)
        if (grant != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(permission)
            requestPermissions(context as android.app.Activity, permissions, 1)
        }
        // read permission
        val permission2 = android.Manifest.permission.READ_EXTERNAL_STORAGE
        val grant2 = context.checkCallingOrSelfPermission(permission2)
        if (grant2 != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(permission2)
            requestPermissions(context as android.app.Activity, permissions, 1)
        }
    }

    private fun createScreenshotDirectory ():Boolean{
        val fileDirectory = File(Environment.getExternalStorageDirectory().absolutePath,"ScreenShots")
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs()
        }
        return true
    }







}