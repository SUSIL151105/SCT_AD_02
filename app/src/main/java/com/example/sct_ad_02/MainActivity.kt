package com.example.sct_ad_02


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()
    private var nextId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupClickListeners()
        addSampleTodos()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvTodos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        todoAdapter = TodoAdapter(
            todos = todoList,
            onTodoClick = { todo ->
                toggleTodoCompletion(todo)
            },
            onTodoLongClick = { todo ->
                showTodoOptions(todo)
            }
        )
        recyclerView.adapter = todoAdapter
    }

    private fun setupClickListeners() {
        val btnAdd = findViewById<Button>(R.id.btnAddTodo)
        val etTodoTitle = findViewById<EditText>(R.id.etTodoTitle)
        val btnClearCompleted = findViewById<Button>(R.id.btnClearCompleted)
        val btnClearAll = findViewById<Button>(R.id.btnClearAll)

        btnAdd.setOnClickListener {
            val title = etTodoTitle.text.toString().trim()
            if (title.isNotEmpty()) {
                addTodo(title)
                etTodoTitle.text.clear()
            } else {
                Toast.makeText(this, "Please enter a todo", Toast.LENGTH_SHORT).show()
            }
        }

        etTodoTitle.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                val title = etTodoTitle.text.toString().trim()
                if (title.isNotEmpty()) {
                    addTodo(title)
                    etTodoTitle.text.clear()
                }
                true
            } else {
                false
            }
        }

        btnClearCompleted.setOnClickListener {
            clearCompletedTodos()
        }

        btnClearAll.setOnClickListener {
            clearAllTodos()
        }
    }

    private fun addTodo(title: String) {
        val newTodo = Todo(id = nextId++, title = title)
        todoList.add(0, newTodo)
        todoAdapter.updateTodos(todoList)
        Toast.makeText(this, "Todo added!", Toast.LENGTH_SHORT).show()
    }

    private fun toggleTodoCompletion(todo: Todo) {
        todo.isCompleted = !todo.isCompleted
        todoAdapter.updateTodos(todoList)
    }

    private fun showTodoOptions(todo: Todo) {
        val options = arrayOf("Edit", "Delete", "Cancel")
        AlertDialog.Builder(this)
            .setTitle("Todo Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editTodo(todo)
                    1 -> deleteTodo(todo)
                }
            }
            .show()
    }

    private fun editTodo(todo: Todo) {
        val editText = EditText(this).apply {
            setText(todo.title)
            setSelection(todo.title.length)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Todo")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = editText.text.toString().trim()
                if (newTitle.isNotEmpty()) {
                    todoList.find { it.id == todo.id }?.title = newTitle
                    todoAdapter.updateTodos(todoList)
                    Toast.makeText(this, "Todo updated!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteTodo(todo: Todo) {
        AlertDialog.Builder(this)
            .setTitle("Delete Todo")
            .setMessage("Are you sure you want to delete this todo?")
            .setPositiveButton("Delete") { _, _ ->
                todoList.removeAll { it.id == todo.id }
                todoAdapter.updateTodos(todoList)
                Toast.makeText(this, "Todo deleted!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun clearCompletedTodos() {
        val completedCount = todoList.count { it.isCompleted }
        if (completedCount > 0) {
            AlertDialog.Builder(this)
                .setTitle("Clear Completed")
                .setMessage("Delete all $completedCount completed todos?")
                .setPositiveButton("Delete") { _, _ ->
                    todoList.removeAll { it.isCompleted }
                    todoAdapter.updateTodos(todoList)
                    Toast.makeText(this, "Completed todos cleared!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "No completed todos to clear", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearAllTodos() {
        if (todoList.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Clear All")
                .setMessage("Delete all ${todoList.size} todos?")
                .setPositiveButton("Delete All") { _, _ ->
                    todoList.clear()
                    todoAdapter.updateTodos(todoList)
                    Toast.makeText(this, "All todos cleared!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "No todos to clear", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addSampleTodos() {
        val sampleTodos = listOf(
            "Learn Kotlin",
            "Build Android App",
            "Add new features",
            "Test the application"
        )
        sampleTodos.forEach { todo ->
            addTodo(todo)
        }
    }
}