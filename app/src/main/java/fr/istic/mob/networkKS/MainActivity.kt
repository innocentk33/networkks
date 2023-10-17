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

    lateinit var vue : VueGenerale //Graph est la vue qui doit etre afficher
    lateinit var graph : DrawEngine //moteur de dessin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vue = VueGenerale(this,null,0,0)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //setContentView(myview(this));s
        //setBackground(context.getResources().getDrawable(R.drawable.roundbg));
        vue.background = ContextCompat.getDrawable(applicationContext,R.drawable.plan)
        //setContentView(vue)
        //Creer un menu pour cette vue (ajout, connexion, edition)

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


    internal class myview(context: Context?) : View(context) {
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val x = 800
            val y = 80
            val radius = 40
            val paint = Paint()
            // Use Color.parseColor to define HTML colors
            paint.color = Color.parseColor("#CD5C5C")
            canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), paint)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when (item.itemId) {
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