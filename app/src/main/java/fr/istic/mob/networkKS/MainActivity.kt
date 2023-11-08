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
               // drawZone.sendDrawZoneByMail(drawZone)
                Utils().sendMail(drawZone,this)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


     fun popupMenuObjet(objet: Objet) {
        val objetView = View(drawZone.context)
        objetView.x = objet.positionX
        objetView.y = objet.positionY
        objetView.visibility = View.VISIBLE
        objetView.layoutParams = FrameLayout.LayoutParams(Objet.rectWidth.toInt(), Objet.rectHeight.toInt())
         val context = objetView.context

        val popupMenu = PopupMenu(context, objetView, Gravity.START)
        popupMenu.inflate(R.menu.context_menu_objet)
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.edit_object -> {
                    //afficher une boite de dialogue pour saisir le nom de l'objet
                    val alertDialog =
                        AlertDialog.Builder(context).setTitle(R.string.alerteDialog_title)
                            .setMessage(R.string.alerteDialog_message)
                    val editText = EditText(context)
                    alertDialog.setView(editText)
                    alertDialog.setPositiveButton(R.string.alerteDialog_confirm) { dialog, which ->
                        objet.label = editText.text.toString()
                       // invalidate()
                    }
                    alertDialog.setNegativeButton(R.string.alerteDialog_cancel) { dialog, which ->
                        dialog.cancel()
                    }
                    alertDialog.show()
                    true
                }

                R.id.delete_object -> {
                  //  graph.objets.remove(objet)
                   // invalidate()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()


    }



}