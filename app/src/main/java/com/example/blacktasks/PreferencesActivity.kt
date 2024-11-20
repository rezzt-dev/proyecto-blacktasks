package com.example.blacktasks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.blacktasks.databinding.ActivityPreferencesBinding
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

/**
 * Actividad que maneja las preferencias del usuario. Permite al usuario cambiar configuraciones como el idioma de la aplicación.
 */
class PreferencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferencesBinding

    /**
     * Método que se llama al crear la actividad. Establece el diseño de la actividad y carga el fragmento de preferencias.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la barra de herramientas
        val toolbar: MaterialToolbar = binding.mainMenu
        setSupportActionBar(toolbar)

        // Carga el fragmento de preferencias al iniciar la actividad
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, SettingsFragment())
            .commit()
    }

    /**
     * Fragmento que maneja las preferencias de la aplicación.
     */
    class SettingsFragment : PreferenceFragmentCompat() {
        /**
         * Método que se llama al crear el fragmento de preferencias.
         * Aquí se cargan las preferencias desde el archivo XML y se configura el manejador de cambios para el idioma.
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            // Configura el listener para cambios en la preferencia del idioma
            val languagePreference = findPreference<ListPreference>("pref_language")
            languagePreference?.setOnPreferenceChangeListener { _, newValue ->
                val languageCode = newValue as String
                updateLanguage(languageCode)
                true
            }
        }

        /**
         * Actualiza el idioma de la aplicación según la selección del usuario.
         * @param languageCode El código del idioma seleccionado.
         */
        private fun updateLanguage(languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = resources.configuration
            config.setLocale(locale)
            activity?.createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)

            // Forza la recreación de la actividad principal para aplicar el cambio de idioma
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    /**
     * Método que se llama cuando la actividad es destruida. Marca que las preferencias han cambiado.
     */
    override fun onDestroy() {
        super.onDestroy()

        // Indica que las preferencias han cambiado y se deben aplicar
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("preferences_changed", true).apply()
    }

    /**
     * Infla el menú de opciones para esta actividad, permitiendo la navegación entre actividades.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Maneja las selecciones de los elementos del menú de opciones.
     * @param item El ítem del menú que ha sido seleccionado.
     * @return Verdadero si el ítem fue procesado correctamente, falso de lo contrario.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_main_window -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) // Abre la actividad principal
            }

            R.id.ic_web_page -> {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://educamosclm.castillalamancha.es/"))
                startActivity(webIntent) // Abre la página web en el navegador
            }

            R.id.ic_acerca_de -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent) // Abre la actividad de información
            }

            R.id.ic_list_window -> {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent) // Abre la actividad de lista de tareas
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
