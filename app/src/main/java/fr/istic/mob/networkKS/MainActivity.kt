package fr.istic.mob.networkKS

import android.graphics.Path
import android.graphics.PointF
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import fr.istic.mob.networkKS.models.Objet


class MainActivity : AppCompatActivity() {


    lateinit var drawZone : DrawZone //moteur de dessin
    var selectedObject: Objet? = null
    var tempPath = Path()
    var tempStartPoint = PointF()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //ajout de la toolbar
        val drawZone = findViewById<FrameLayout>(R.id.drawZone)
        this.drawZone = DrawZone(this,null,0)
        this.drawZone.mode = Mode.MOVE
        drawZone.addView(this.drawZone)


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
                drawZone.mode = Mode.ADD
                Toast.makeText(this,"Mode Ajout", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mode_connect -> {
               // modeConnexion()
                drawZone.mode = Mode.CONNECT
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                selectedObject = drawZone.findObjectAtPoint(event.x, event.y)
                if (selectedObject != null) {
                    tempPath.reset()
                    tempStartPoint.set(selectedObject!!.position)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (selectedObject != null) {
                    // L'utilisateur déplace son doigt, met à jour le chemin temporaire
                    tempPath.reset()
                    tempPath.moveTo(tempStartPoint.x, tempStartPoint.y)
                    tempPath.lineTo(event.x, event.y)
                    drawZone.invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (selectedObject != null) {
                    val endObject = drawZone.findObjectAtPoint(event.x, event.y)
                    if (endObject != null && selectedObject != endObject) {
                        // Créez une connexion en ligne droite entre les deux objets
                        drawZone.createConnection(selectedObject!!, endObject)
                        drawZone.invalidate()
                    }
                }
                selectedObject = null
                tempPath.reset()
            }
        }
        return super.onTouchEvent(event)
    }

}