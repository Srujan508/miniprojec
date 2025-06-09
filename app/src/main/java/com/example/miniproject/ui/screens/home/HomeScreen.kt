package com.example.miniproject.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.miniproject.data.model.Medicine
import com.example.miniproject.ui.components.*
import com.example.miniproject.ui.screens.add.AddMedicineScreen
import com.example.miniproject.ui.viewmodel.MedicineViewModel
import com.example.miniproject.ui.viewmodel.ChatbotViewModel
import com.example.miniproject.ui.viewmodel.AuthViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    viewModel: MedicineViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var showAddMedicineDialog by remember { mutableStateOf(false) }
    var showChatbotDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showStatisticsDialog by remember { mutableStateOf(false) }
    val medicines by viewModel.medicines.collectAsState()
    val error by viewModel.error.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        if (authState is AuthViewModel.AuthState.Authenticated) {
            viewModel.loadMedicines(authViewModel.currentUser?.uid ?: "")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medicine Reminder") },
                actions = {
                    IconButton(onClick = { showStatisticsDialog = true }) {
                        Icon(Icons.Default.BarChart, contentDescription = "Statistics")
                    }
                    IconButton(onClick = { showChatbotDialog = true }) {
                        Icon(Icons.Default.Chat, contentDescription = "Chatbot")
                    }
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { 
                        authViewModel.signOut()
                        onSignOut()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMedicineDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Medicine")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (medicines.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No medicines added yet.\nTap + to add your first medicine.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = medicines,
                            key = { it.id }
                        ) { medicine ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                MedicineCard(
                                    medicine = medicine,
                                    onMarkAsTaken = { viewModel.updateMedicineStatus(medicine, true) },
                                    onMarkAsMissed = { viewModel.updateMedicineStatus(medicine, false) },
                                    onDelete = { viewModel.deleteMedicine(medicine) }
                                )
                            }
                        }
                    }
                }
            }

            if (showAddMedicineDialog) {
                AddMedicineScreen(
                    onNavigateBack = { showAddMedicineDialog = false }
                )
            }

            if (showChatbotDialog) {
                ChatbotDialog(
                    onDismiss = { showChatbotDialog = false },
                    viewModel = hiltViewModel()
                )
            }

            if (showSettingsDialog) {
                SettingsDialog(
                    onDismiss = { showSettingsDialog = false }
                )
            }

            if (showStatisticsDialog) {
                StatisticsDialog(
                    medicines = medicines,
                    onDismiss = { showStatisticsDialog = false }
                )
            }

            error?.let { errorMessage ->
                ErrorDialog(
                    message = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )
            }
        }
    }
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add settings options here
                ListItem(
                    headlineContent = { Text("Notification Sound") },
                    leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    trailingContent = { Switch(checked = true, onCheckedChange = {}) }
                )

                ListItem(
                    headlineContent = { Text("Dark Theme") },
                    leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                    trailingContent = { Switch(checked = false, onCheckedChange = {}) }
                )

                ListItem(
                    headlineContent = { Text("Backup Data") },
                    leadingContent = { Icon(Icons.Default.Backup, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun StatisticsDialog(
    medicines: List<Medicine>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add statistics here
                ListItem(
                    headlineContent = { Text("Total Medicines") },
                    supportingContent = { Text(medicines.size.toString()) }
                )

                ListItem(
                    headlineContent = { Text("Taken Today") },
                    supportingContent = { Text(medicines.count { it.isTaken }.toString()) }
                )

                ListItem(
                    headlineContent = { Text("Missed Today") },
                    supportingContent = { Text(medicines.count { !it.isTaken }.toString()) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineCard(
    medicine: Medicine,
    onMarkAsTaken: () -> Unit,
    onMarkAsMissed: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Show medicine details */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Dosage: ${medicine.dosage}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Times to Take:",
                style = MaterialTheme.typography.bodyMedium
            )
            medicine.times.forEach { time ->
                Text(
                    text = "• ${time.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            medicine.notes?.let { notes ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: $notes",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onMarkAsTaken,
                    enabled = !medicine.isTaken,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mark as Taken")
                }

                Button(
                    onClick = onMarkAsMissed,
                    enabled = medicine.isTaken,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mark as Missed")
                }
            }
        }
    }
}

@Composable
fun ChatbotDialog(
    onDismiss: () -> Unit,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    var message by remember { mutableStateOf("") }
    val chatHistory by viewModel.chatHistory.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chat with AI Assistant",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(chatHistory) { (userMessage, botResponse) ->
                        Column {
                            ChatMessage("You: $userMessage", true)
                            ChatMessage("Bot: $botResponse", false)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = "Type your message",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (message.isNotBlank()) {
                                viewModel.sendMessage(message)
                                message = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessage(message: String, isUser: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUser) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isUser) 
                MaterialTheme.colorScheme.onPrimaryContainer 
            else 
                MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
} 