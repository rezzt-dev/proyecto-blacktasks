package com.example.blacktasks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.blacktasks.databinding.ActivityInfoBinding
import com.google.android.material.appbar.MaterialToolbar

/**
 * Actividad que muestra información adicional sobre la aplicación.
 * Esta actividad contiene un menú de opciones que permite navegar a otras actividades y abrir una página web.
 */
class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la barra de herramientas como ActionBar
        val toolbar: MaterialToolbar = binding.mainMenu
        setSupportActionBar(toolbar)

        // Configura el borde de la pantalla para un aspecto visual más atractivo
        enableEdgeToEdge()
    }

    /**
     * Inflar el menú de opciones de la actividad.
     * Se utiliza para agregar elementos de menú como íconos y acciones.
     *
     * @param menu El objeto Menu al que se deben agregar los elementos.
     * @return True si el menú fue inflado correctamente.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Manejar la selección de elementos de menú.
     * Se encarga de iniciar las actividades correspondientes o realizar las acciones definidas.
     *
     * @param item El objeto MenuItem seleccionado.
     * @return True si se manejó la selección correctamente.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_main_window -> {
                // Inicia la actividad principal
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.ic_preferences -> {
                // Inicia la actividad de preferencias
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
            }

            R.id.ic_web_page -> {
                // Abre la página web en un navegador
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://educamosclm.castillalamancha.es/"))
                startActivity(webIntent)
            }

            R.id.ic_list_window -> {
                // Inicia la actividad de lista
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
