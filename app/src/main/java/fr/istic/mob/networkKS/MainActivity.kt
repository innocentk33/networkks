package fr.istic.mob.networkKS

import android.graphics.Path
import android.graphics.PointF
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import fr.istic.mob.networkKS.models.Objet
import fr.istic.mob.networkKS.utils.Utils
import java.io.File


class MainActivity : AppCompatActivity() {


    private lateinit var drawZone : DrawZone //moteur de dessin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //ajout de la toolbar
        val drawView = findViewById<FrameLayout>(R.id.drawZone)
        this.drawZone = DrawZone(this)
        this.drawZone.mode = Mode.ADD
        drawView.addView(this.drawZone)
        Utils().askPermission(this)
        registerForContextMenu(drawView)

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
                drawZone.viewSavedNetwork()
                Toast.makeText(this,R.string.view_saved_network, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.sendMail->{
                // envoyer la capture d'écran par mail
               // drawZone.sendDrawZoneByMail(drawZone)
                Utils().sendMail(drawZone,this)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_objet,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete_object->{
                //drawZone.deleteObjet()
                true
            }
            R.id.edit_object->{
               // drawZone.editObjet()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }



}