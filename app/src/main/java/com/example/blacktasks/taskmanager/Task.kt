package com.example.blacktasks.taskmanager

/**
 * Representa una tarea dentro de la aplicación.
 * Una tarea tiene un identificador único, un título, una descripción y un estado que indica si está completada o no.
 *
 * @property id El identificador único de la tarea.
 * @property title El título de la tarea.
 * @property description La descripción detallada de la tarea.
 * @property isCompleted Indica si la tarea está completada o no. El valor predeterminado es 'false'.
 */
data class Task(
    val id: Int, // Identificador único de la tarea
    val title: String, // Título de la tarea
    val description: String, // Descripción detallada de la tarea
    var isCompleted: Boolean = false // Estado que indica si la tarea está completada (por defecto es 'false')
)
