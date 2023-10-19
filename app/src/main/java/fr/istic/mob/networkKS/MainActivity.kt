package fr.istic.mob.networkKS

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    lateinit var graph : DrawEngine //moteur de dessin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //ajout de la toolbar
        val drawZone = findViewById<FrameLayout>(R.id.drawZone)
        graph = DrawEngine(this,null,0)
        drawZone.addView(graph)

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
                Toast.makeText(this,"Reseau Reinitialisé", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_add -> {
                //modeAdd()
                Toast.makeText(this,"Mode Ajout", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_connect -> {
               // modeConnexion()
                Toast.makeText(this,"Mode Connexion", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_edit -> {
                // modeEdition()
                Toast.makeText(this,"Mode Edition", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}