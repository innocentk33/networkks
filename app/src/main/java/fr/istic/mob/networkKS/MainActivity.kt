package fr.istic.mob.networkKS
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import fr.istic.mob.networkKS.models.Objet
import fr.istic.mob.networkKS.utils.Utils

class MainActivity : AppCompatActivity() {


    private lateinit var drawZone : DrawZone //moteur de dessin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //ajout de la toolbar
        val drawView = findViewById<FrameLayout>(R.id.drawZone)
        //drawView.background = AppCompatResources.getDrawable(this, R.drawable.plan) //ajout du plan
        this.drawZone = DrawZone(this)
        this.drawZone.mode = Mode.ADD
        drawView.addView(this.drawZone)
        Utils().askPermission(this)

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
                val drawView = findViewById<FrameLayout>(R.id.drawZone)
                Utils().sendMail(drawView)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}