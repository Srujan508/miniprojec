package com.example.miniproject.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.miniproject.data.model.Medicine
import com.example.miniproject.ui.components.TimePicker
import com.example.miniproject.ui.viewmodel.AuthViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddMedicineViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var times by remember { mutableStateOf(listOf(LocalTime.now())) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val userId = authViewModel.currentUser?.uid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Medicine") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Medicine Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError && name.isBlank()
            )

            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("Dosage") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError && dosage.isBlank()
            )

            Text(
                text = "Reminder Times",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            times.forEachIndexed { index, time ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimePicker(
                        time = time,
                        onTimeChange = { newTime ->
                            times = times.toMutableList().apply {
                                set(index, newTime)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    if (times.size > 1) {
                        IconButton(
                            onClick = {
                                times = times.toMutableList().apply {
                                    removeAt(index)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Time",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    times = times + LocalTime.now()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Time"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Another Time")
            }

            Spacer(modifier = Modifier.weight(1f))

            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (name.isBlank() || dosage.isBlank()) {
                        showError = true
                        errorMessage = "Please fill in all required fields"
                        return@Button
                    }
                    if (times.isEmpty()) {
                        errorMessage = "Please add at least one reminder time"
                        return@Button
                    }
                    if (userId == null) {
                        errorMessage = "Please sign in to add medicines"
                        return@Button
                    }
                    viewModel.addMedicine(
                        Medicine(
                            name = name,
                            dosage = dosage,
                            times = times,
                            userId = userId
                        )
                    )
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Medicine")
            }
        }
    }
} 