package com.example.sct_ad_02



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private var todos: List<Todo>,
    private val onTodoClick: (Todo) -> Unit,
    private val onTodoLongClick: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTodoTitle)
        private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)

        fun bind(todo: Todo) {
            tvTitle.text = todo.title
            cbCompleted.isChecked = todo.isCompleted

            // Strike through text when completed
            if (todo.isCompleted) {
                tvTitle.paintFlags = tvTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            itemView.setOnClickListener {
                onTodoClick(todo)
            }

            itemView.setOnLongClickListener {
                onTodoLongClick(todo)
                true
            }

            cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                todo.isCompleted = isChecked
                if (isChecked) {
                    tvTitle.paintFlags = tvTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvTitle.paintFlags = tvTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int = todos.size

    fun updateTodos(newTodos: List<Todo>) {
        todos = newTodos
        notifyDataSetChanged()
    }
}