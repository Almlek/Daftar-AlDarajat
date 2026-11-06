package com.alborihi.myapp.presentation.ui.screens.grades

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.alborihi.myapp.domain.model.Month
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeEntryScreen(
    viewModel: GradeEntryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "إدخال الدرجات",
                showBackButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Student Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "اختيار طالب",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.students.isEmpty()) {
                        Text(
                            text = "لا يوجد طلاب. أضف طالباً أولاً.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.students) { student ->
                                StudentChip(
                                    student = student,
                                    onClick = { viewModel.onStudentSelected(student) },
                                    isSelected = state.selectedStudent?.id == student.id
                                )
                            }
                        }
                    }
                }
            }

            // Month & Term Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "الشهر",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = state.selectedMonth.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "الترم",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = state.selectedTerm.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Subjects
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "المواد",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.subjects.keys.toList()) { subject ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = subject,
                                    onValueChange = { /* subject name fixed */ },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("المادة") },
                                    trailingIcon = {
                                        IconButton(onClick = { viewModel.removeSubject(subject) }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "إزالة"
                                            )
                                        }
                                    }
                                )
                                OutlinedTextField(
                                    value = state.subjects[subject].toString(),
                                    onValueChange = { score ->
                                        viewModel.onSubjectScoreChange(subject, score.toDoubleOrNull() ?: 0.0)
                                    },
                                    modifier = Modifier.width(120.dp),
                                    label = { Text("الدرجة") },
                                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.foundation.text.KeyboardType.Decimal)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                val subjectName = "مادة جديدة"
                                viewModel.addSubject(subjectName)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("إضافة مادة")
                        }
                    }
                }
            }

            // Save Button
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                text = "حفظ الدرجات",
                onClick = { viewModel.saveGrade() },
                isLoading = state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    state.snackbarMessage?.let { message ->
        Snackbar(
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            onDismissRequest = { viewModel.dismissSnackbar() }
        ) {
            Text(text = message)
        }
    }
}
