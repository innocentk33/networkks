package fr.istic.mob.networkKS

import android.graphics.Path
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import fr.istic.mob.networkKS.models.Objet


class MainActivity : AppCompatActivity() {


    private lateinit var drawZone : DrawZone //moteur de dessin
    var selectedObject: Objet? = null
    var tempPath = Path()
    var tempStartPoint = PointF()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //ajout de la toolbar
        val drawView = findViewById<FrameLayout>(R.id.drawZone)
        this.drawZone = DrawZone(this)
        this.drawZone.mode = Mode.ADD
        drawView.addView(this.drawZone)



        // Créez une instance de la classe Objet
        val objet = Objet(positionX = 100.0f, positionY = 200.0f, label = "Objet 1")

// Initialisez la bibliothèque GSON
        val gson = Gson()

// Sérialisez l'objet en JSON
        val json = gson.toJson(objet)
        Log.d("Objet", json)
    }


   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
          //Ajout des elements au menu
          val inflater = this.menuInflater
          inflater.inflate(R.menu.nav_bar, menu)
          return super.onCreateOptionsMenu(menu)
      }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when (item.itemId) {
            R.id.mode_reset->{
                this.recreate()
                Toast.makeText(this,R.string.mode_reset, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_add -> {
                drawZone.mode = Mode.ADD
                Toast.makeText(this,R.string.mode_add, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_connect -> {
                drawZone.mode = Mode.CONNECT
                Toast.makeText(this,R.string.mode_connect, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_edit -> {
                drawZone.mode = Mode.EDIT
                Toast.makeText(this,R.string.mode_edit, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_move->{
                drawZone.mode = Mode.MOVE
                Toast.makeText(this,R.string.mode_move, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.saveNetwork->{
                drawZone.saveGraph()
               true
            }
            R.id.viewSavedNetwork->{
                //drawZone.viewSavedNetwork()
                Toast.makeText(this,R.string.view_saved_network, Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



}