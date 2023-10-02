package fr.istic.mob.networkKS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Ajout des elements au menu
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nav_bar, menu)
        return true
    }


    /*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mode_add -> Toast.makeText(this, "About Selected", Toast.LENGTH_SHORT).show()
            R.id.mode_connect -> Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
            R.id.mode_edit -> Toast.makeText(this, "Exit Selected", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}