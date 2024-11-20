package com.example.blacktasks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.blacktasks.databinding.ActivityListBinding
import com.example.blacktasks.taskmanager.TaskPagerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Actividad que maneja la interfaz de la lista de tareas.
 * Utiliza un ViewPager2 para mostrar diferentes fragmentos, cada uno representando un conjunto de tareas (todas, incompletas y completadas).
 */
class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la barra de herramientas como ActionBar
        val toolbar: MaterialToolbar = binding.mainMenu
        setSupportActionBar(toolbar)

        // Inicializa los elementos de ViewPager2 y TabLayout
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.optionsTabs)

        // Configura el adaptador para el ViewPager2, que maneja los fragmentos
        val pagerAdapter = TaskPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter

        // Conecta el TabLayout con el ViewPager2 para mostrar las pestañas correspondientes
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.list_all_task) // Pestaña para todas las tareas
                1 -> getString(R.string.list_incompleted_task) // Pestaña para las tareas incompletas
                2 -> getString(R.string.list_completed_task) // Pestaña para las tareas completadas
                else -> null
            }
        }.attach()
    }

    /**
     * Infla el menú de opciones de la actividad.
     * Se agregan los elementos del menú para permitir la navegación a otras actividades y acciones.
     *
     * @param menu El objeto Menu en el que se agregarán los elementos.
     * @return True si el menú se infla correctamente.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Maneja la selección de los elementos del menú.
     * Permite navegar entre diferentes actividades o realizar acciones específicas.
     *
     * @param item El elemento del menú que fue seleccionado.
     * @return True si la acción fue manejada correctamente.
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

            R.id.ic_acerca_de -> {
                // Inicia la actividad de información sobre la aplicación
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}