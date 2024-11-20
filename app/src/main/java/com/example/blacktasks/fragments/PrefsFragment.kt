package com.example.blacktasks.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.blacktasks.R

/**
 * Fragmento de preferencias que permite la gestión de la configuración de la aplicación.
 * Este fragmento está diseñado para mostrar y gestionar las preferencias del usuario, como el nombre de usuario
 * y las configuraciones de notificaciones.
 */
class PrefsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Método que se llama para crear las preferencias del fragmento.
     * Configura las preferencias del usuario a partir de un archivo de recursos XML.
     *
     * @param savedInstanceState El estado guardado del fragmento, si existe.
     * @param rootKey La clave raíz para las preferencias (puede ser null).
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Establece las preferencias a partir del archivo de recursos XML
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    /**
     * Método que se llama cuando el fragmento se reanuda.
     * Registra el fragmento como escuchador de cambios en las preferencias compartidas.
     */
    override fun onResume() {
        super.onResume()
        // Registra el escuchador para cambios en las preferencias
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    /**
     * Método que se llama cuando el fragmento se pausa.
     * Desregistra el fragmento como escuchador de cambios en las preferencias compartidas.
     */
    override fun onPause() {
        super.onPause()
        // Desregistra el escuchador para cambios en las preferencias
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * Método que se llama cuando una preferencia ha cambiado.
     * Gestiona los cambios de preferencias, como el nombre de usuario y la configuración de notificaciones.
     *
     * @param sharedPreferences Las preferencias compartidas en las que se realizó el cambio.
     * @param key La clave de la preferencia que cambió.
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            // Si se cambia la preferencia del nombre de usuario
            "pref_username" -> {
                // Obtiene el nuevo nombre de usuario, con un valor por defecto de "Usuario"
                val userName = sharedPreferences?.getString(key, "Usuario") ?: "Usuario"
                // Guarda el nombre de usuario en las preferencias compartidas
                sharedPreferences?.edit()?.putString("saved_username", userName)?.apply()
            }

            // Si se cambia la preferencia de notificaciones
            "pref_notifications" -> {
                // Obtiene el estado de las notificaciones, con un valor por defecto de 'true' (activadas)
                val isNotificacionEnabled = sharedPreferences?.getBoolean(key, true) ?: true
                // Guarda el estado de las notificaciones en las preferencias compartidas
                sharedPreferences?.edit()?.putBoolean("saved_notifications", isNotificacionEnabled)?.apply()
            }
        }
    }
}
