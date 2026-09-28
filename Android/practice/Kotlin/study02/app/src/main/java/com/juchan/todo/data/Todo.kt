package com.juchan.todo.data

data class Todo(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)
