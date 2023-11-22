package fr.istic.mob.networkKS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import fr.istic.mob.networkKS.utils.Utils

class MainActivity : AppCompatActivity() {


    private lateinit var drawZone : DrawZone //moteur de dessin
    private lateinit var drawView : FrameLayout //zone de dessin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //ajout de la toolbar
        drawView = findViewById<FrameLayout>(R.id.drawZone)
        drawView.background = AppCompatResources.getDrawable(this, R.drawable.plan) //ajout du plan
        //choix du plan
        choseBackground()
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

            R.id.viewImportSavedNetwork->{
                drawZone.viewImportNetwork()
                Toast.makeText(this,"Importer un réseau sauvegardé", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.sendMail->{
                // envoyer la capture d'écran par mail
                val drawView = findViewById<FrameLayout>(R.id.drawZone)
                Utils().sendMail(drawView)
                true
            }
            R.id.choseBackground->{
                choseBackground()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
// peut mieux faire mais je n'ai pas eu le temps de faire mieux
   private fun choseBackground (){
    val chosePlanLayout = LayoutInflater.from(this).inflate(R.layout.chose_plan, null)
    val plan0 = chosePlanLayout.findViewById<RadioButton>(R.id.graphe0)
    val plan1 = chosePlanLayout.findViewById<RadioButton>(R.id.graphe1)
    val plan2 = chosePlanLayout.findViewById<RadioButton>(R.id.graphe2)
    val plan3 = chosePlanLayout.findViewById<RadioButton>(R.id.graphe3)
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.setTitle(R.string.chosePlan)
    alertDialog.setView(chosePlanLayout)
    alertDialog.setPositiveButton(R.string.alerteDialog_confirm) { _, _ ->
        if (plan0.isChecked) {
            drawView.background = AppCompatResources.getDrawable(this, R.drawable.plan)
        } else if (plan1.isChecked) {
            drawView.background = AppCompatResources.getDrawable(this, R.drawable.plan1)
        } else if (plan2.isChecked) {
            drawView.background = AppCompatResources.getDrawable(this, R.drawable.plan2)
        } else if (plan3.isChecked) {
            drawView.background = AppCompatResources.getDrawable(this, R.drawable.plan3)
        }

    }
    alertDialog.setNegativeButton("Cancel") { dialog, which ->
        dialog.cancel()
    }
    alertDialog.show()
    }
}