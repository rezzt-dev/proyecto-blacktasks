package com.example.blacktasks.taskmanager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blacktasks.R

/**
 * Adaptador para mostrar y gestionar una lista de tareas en un RecyclerView.
 * Este adaptador se encarga de enlazar las tareas con las vistas y permitir la interacción con ellas,
 * como marcar tareas como completadas o filtrar las tareas por título.
 *
 * @property tasks La lista mutable de tareas que se mostrará en el RecyclerView.
 */
class TaskAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    // Lista de tareas filtradas para mostrar en el RecyclerView.
    private var filteredTasks: List<Task> = tasks.toList()

    /**
     * Clase que representa un elemento de la vista en el RecyclerView.
     * Contiene referencias a las vistas que mostrarán los detalles de cada tarea.
     *
     * @param view La vista de un ítem individual del RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.taskCheckBox) // Checkbox para marcar si la tarea está completada
        val titleTextView: TextView = view.findViewById(R.id.taskTitle) // TextView para mostrar el título de la tarea
        val descriptionTextView: TextView = view.findViewById(R.id.taskDescription) // TextView para mostrar la descripción de la tarea
    }

    /**
     * Crea una nueva vista para un elemento del RecyclerView.
     * Se llama cuando se necesita una nueva vista para un ítem en la lista.
     *
     * @param parent El contenedor que albergará el nuevo ítem.
     * @param viewType El tipo de vista, que se puede usar para manejar diferentes tipos de ítems.
     * @return Un nuevo ViewHolder con la vista inflada.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false) // Infla el layout para un ítem de tarea
        return ViewHolder(view)
    }

    /**
     * Asocia los datos de una tarea a las vistas correspondientes en el ViewHolder.
     * Se llama para cada ítem cuando el RecyclerView necesita mostrarlo.
     *
     * @param holder El ViewHolder donde se deben asignar los datos.
     * @param position La posición de la tarea dentro de la lista.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position] // Obtiene la tarea en la posición dada
        holder.titleTextView.text = task.title // Establece el título de la tarea
        holder.descriptionTextView.text = task.description // Establece la descripción de la tarea
        holder.checkBox.isChecked = task.isCompleted // Establece si la tarea está completada según el estado

        // Establece el listener para cuando se cambia el estado del checkbox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked // Actualiza el estado de la tarea
            // Aquí puedes agregar lógica adicional cuando se marca/desmarca una tarea
        }
    }

    /**
     * Devuelve el número total de ítems en la lista de tareas.
     *
     * @return El número de tareas en la lista.
     */
    override fun getItemCount() = tasks.size

    /**
     * Filtra la lista de tareas según el título de la tarea.
     * Si el texto de búsqueda es vacío, se muestra todas las tareas.
     *
     * @param query El texto con el cual se filtrarán las tareas por título.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filteredTasks = if (query.isEmpty()) {
            tasks.toList() // Si la búsqueda está vacía, muestra todas las tareas
        } else {
            tasks.filter {
                it.title.contains(query, ignoreCase = true) // Filtra por el título de la tarea
            }
        }
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    /**
     * Añade una nueva tarea a la lista de tareas y notifica al RecyclerView que un ítem ha sido insertado.
     *
     * @param task La tarea a añadir.
     */
    fun addTask(task: Task) {
        tasks.add(task) // Añade la tarea a la lista
        notifyItemInserted(tasks.size - 1) // Notifica que se ha añadido un nuevo ítem
    }

    /**
     * Elimina todas las tareas que están marcadas como completadas.
     * Luego, notifica al RecyclerView que los datos han cambiado.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun removeCheckedItems() {
        tasks.removeAll { it.isCompleted } // Elimina las tareas completadas
        notifyDataSetChanged() // Notifica que los datos han cambiado
    }

    /**
     * Actualiza la lista de tareas y notifica al RecyclerView que los datos han cambiado.
     *
     * @param newTasks La nueva lista de tareas que se mostrará en el RecyclerView.
     */
    fun submitList(newTasks: List<Task>) {
        tasks.clear() // Limpia la lista actual de tareas
        tasks.addAll(newTasks) // Agrega todas las nuevas tareas a la lista
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}
