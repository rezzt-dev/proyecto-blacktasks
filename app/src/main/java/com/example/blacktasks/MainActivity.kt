package com.example.blacktasks

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blacktasks.databinding.ActivityMainBinding
import com.example.blacktasks.taskmanager.Task
import com.example.blacktasks.taskmanager.TaskAdapter
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

/**
 * Actividad principal de la aplicación. Maneja la interfaz de usuario para mostrar y gestionar tareas.
 * Permite agregar nuevas tareas, eliminar tareas seleccionadas, realizar búsquedas y cambiar configuraciones de idioma.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: TaskAdapter

    companion object {
        private const val PREFERENCES_REQUEST_CODE = 1001 // Código de solicitud para preferencias
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        applyLanguage() // Aplica el idioma configurado en las preferencias

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainMenu) // Establece la barra de herramientas como ActionBar

        setupViews() // Configura los botones y acciones
        setupRecyclerView() // Configura el RecyclerView para mostrar las tareas
    }

    /**
     * Configura las vistas principales, incluyendo los botones para agregar y eliminar tareas.
     */
    private fun setupViews() {
        binding.deleteTask.setOnClickListener { showDeleteConfirmationDialog() } // Acción para eliminar tareas
        binding.addTask.setOnClickListener { showAddTaskDialog() } // Acción para agregar nuevas tareas
    }

    /**
     * Aplica el idioma configurado en las preferencias de la aplicación.
     */
    private fun applyLanguage() {
        val languageCode = sharedPreferences.getString("pref_language", "en") ?: "en" // Obtiene el idioma preferido
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * Verifica si las preferencias de la aplicación han cambiado y recrea la actividad si es necesario.
     */
    override fun onResume() {
        super.onResume()
        if (checkIfPreferencesChanged()) {
            recreate() // Recrea la actividad si las preferencias cambiaron
        }
    }

    /**
     * Comprueba si las preferencias han cambiado y resetea la marca de cambio.
     * @return True si las preferencias han cambiado, False en caso contrario.
     */
    private fun checkIfPreferencesChanged(): Boolean {
        val preferencesChanged = sharedPreferences.getBoolean("preferences_changed", false)
        if (preferencesChanged) {
            sharedPreferences.edit().putBoolean("preferences_changed", false).apply() // Resetea el cambio
            return true
        }
        return false
    }

    /**
     * Configura el RecyclerView para mostrar las tareas.
     * Se utiliza un adaptador personalizado que maneja la lista de tareas.
     */
    private fun setupRecyclerView() {
        adapter = TaskAdapter(mutableListOf()) // Crea un adaptador vacío
        binding.recyclerViewDrivers.adapter = adapter // Asigna el adaptador al RecyclerView
        binding.recyclerViewDrivers.layoutManager = LinearLayoutManager(this) // Establece el layout manager
    }

    /**
     * Muestra un cuadro de diálogo para agregar una nueva tarea.
     */
    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null) // Infla el diseño del diálogo
        val titleEditText = dialogView.findViewById<EditText>(R.id.taskTitleEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.taskDescriptionEditText)

        AlertDialog.Builder(this).setView(dialogView)
            .setPositiveButton(getString(R.string.add_task)) { _, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                if (title.isNotEmpty()) {
                    addNewTask(title, description) // Si el título no está vacío, agrega la tarea
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    /**
     * Muestra un cuadro de diálogo para confirmar la eliminación de las tareas seleccionadas.
     */
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_selected)) // Título del cuadro de diálogo
            .setMessage(getString(R.string.confirm)) // Mensaje de confirmación
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                adapter.removeCheckedItems() // Elimina las tareas seleccionadas
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    /**
     * Muestra un cuadro de diálogo para realizar una búsqueda de tareas.
     */
    private fun showSearchDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT // Tipo de entrada para texto

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.search)) // Título del cuadro de búsqueda
            .setView(input)
            .setPositiveButton(getString(R.string.search)) { _, _ ->
                val searchQuery = input.text.toString()
                performSearch(searchQuery) // Realiza la búsqueda con la consulta proporcionada
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    /**
     * Realiza la búsqueda de tareas en el adaptador.
     * @param query La consulta de búsqueda.
     */
    private fun performSearch(query: String) {
        adapter.filter(query) // Filtra las tareas en el adaptador
    }

    /**
     * Agrega una nueva tarea al adaptador.
     * @param title El título de la nueva tarea.
     * @param description La descripción de la nueva tarea.
     */
    private fun addNewTask(title: String, description: String) {
        val newTask = Task(adapter.itemCount + 1, title, description) // Crea una nueva tarea
        adapter.addTask(newTask) // Añade la tarea al adaptador
        binding.recyclerViewDrivers.scrollToPosition(adapter.itemCount - 1) // Desplaza el RecyclerView al final
    }

    /**
     * Infla el menú de opciones de la actividad.
     * @param menu El objeto Menu donde se inflarán los elementos.
     * @return True si el menú se infla correctamente.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Maneja la selección de los elementos del menú de opciones.
     * @param item El ítem del menú que fue seleccionado.
     * @return True si la acción se maneja correctamente.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_preferences -> {
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent) // Inicia la actividad de preferencias
            }

            R.id.ic_web_page -> {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://educamosclm.castillalamancha.es/"))
                startActivity(webIntent) // Abre la página web en el navegador
            }

            R.id.ic_acerca_de -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent) // Inicia la actividad de información
            }

            R.id.ic_list_window -> {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent) // Inicia la actividad de lista de tareas
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Maneja el resultado de las actividades iniciadas.
     * @param requestCode El código de solicitud.
     * @param resultCode El código de resultado.
     * @param data Los datos devueltos por la actividad.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PREFERENCES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            recreate() // Recrea la actividad si las preferencias han cambiado
        }
    }
}
