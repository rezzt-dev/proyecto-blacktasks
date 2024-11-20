package com.example.blacktasks.taskmanager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blacktasks.fragments.AllTasksFragment
import com.example.blacktasks.fragments.CompletedTasksFragment
import com.example.blacktasks.fragments.NoTasksCompletedFragment

/**
 * Adaptador para gestionar los fragmentos dentro de un ViewPager2.
 * Este adaptador es responsable de proporcionar los fragmentos correspondientes a cada posición en el ViewPager2.
 *
 * @param fragmentManager El FragmentManager que maneja la transacción de fragmentos.
 * @param lifecycle El Lifecycle del ViewPager2 para gestionar el ciclo de vida de los fragmentos.
 */
class TaskPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    /**
     * Devuelve el número total de fragmentos que se manejarán en el ViewPager2.
     *
     * @return El número de fragmentos (en este caso, 3).
     */
    override fun getItemCount(): Int = 3 // Tres fragmentos

    /**
     * Crea el fragmento correspondiente a la posición especificada en el ViewPager2.
     *
     * @param position La posición del fragmento dentro del ViewPager2.
     * @return El fragmento correspondiente a la posición dada.
     * @throws IllegalStateException Si la posición no es válida (aunque no se espera que ocurra).
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllTasksFragment() // Muestra el fragmento de todas las tareas
            1 -> NoTasksCompletedFragment() // Muestra el fragmento de tareas no completadas
            2 -> CompletedTasksFragment() // Muestra el fragmento de tareas completadas
            else -> throw IllegalStateException("Unexpected position $position") // Lanza una excepción si la posición no es válida
        }
    }
}
