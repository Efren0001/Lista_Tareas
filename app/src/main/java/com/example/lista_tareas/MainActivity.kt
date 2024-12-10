package com.example.lista_tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lista_tareas.ui.theme.Lista_TareasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lista_TareasTheme {
                TaskApp()
            }
        }
    }
}

data class Task(
    val title: String,
    val description: String,
    var priority: Priority,
    var isCompleted: Boolean
)

enum class Priority(val color: Color, val label: String) {
    High(Color.Red, "Alta"),
    Medium(Color.Yellow, "Media"),
    Low(Color.Green, "Baja")
}

@Composable
fun TaskApp() {
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task("Comprar comestibles", "Comprar frutas y verduras.", Priority.High, false),
                Task("Estudiar Compose", "Practicar layouts y estados.", Priority.Medium, false),
                Task("Ejercicio", "Salir a correr.", Priority.Low, true)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Tareas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Text(
            text = "Tareas Pendientes",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        tasks.filter { !it.isCompleted }.forEach { task ->
            TaskCard(task = task) { updatedTask ->
                tasks = tasks.map { if (it.title == updatedTask.title) updatedTask else it }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "Tareas Completadas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        tasks.filter { it.isCompleted }.forEach { task ->
            TaskCard(task = task) { updatedTask ->
                tasks = tasks.map { if (it.title == updatedTask.title) updatedTask else it }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onUpdateTask: (Task) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(task.priority.color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(task.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(task.description, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        onUpdateTask(task.copy(isCompleted = !task.isCompleted))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.isCompleted) Color.Green else Color.Red
                    )
                ) {
                    Text(if (task.isCompleted) "Marcar como pendiente" else "Marcar como completada")
                }
                Spacer(modifier = Modifier.width(8.dp))
                PriorityDropdown(task = task, onPriorityChange = { newPriority ->
                    onUpdateTask(task.copy(priority = newPriority))
                })
            }
        }
    }
}

@Composable
fun PriorityDropdown(task: Task, onPriorityChange: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Cambiar Prioridad")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().forEach { priority ->
                DropdownMenuItem(
                    onClick = {
                        onPriorityChange(priority)
                        expanded = false
                    },
                    text = { Text(text = priority.label) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskAppPreview() {
    Lista_TareasTheme {
        TaskApp()
    }
}
