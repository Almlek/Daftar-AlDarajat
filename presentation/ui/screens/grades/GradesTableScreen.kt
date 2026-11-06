package com.alborihi.myapp.presentation.ui.screens.grades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alborihi.myapp.domain.model.Month
import com.alborihi.myapp.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesTableScreen(
    viewModel: GradesTableViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "جدول الدرجات",
                showBackButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.showMonthSelector() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("الشهر: ${state.selectedMonth.displayName}")
                }
                Button(
                    onClick = { /* Term selection */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("الترم: ${state.selectedTerm}")
                }
            }

            // Table
            if (state.students.isEmpty()) {
                EmptyState(message = "لا توجد درجات لعرضها")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.students) { (studentName, grade) ->
                        GradeCell(
                            student = com.alborihi.myapp.domain.model.Student(name = studentName),
                            grade = grade
                        )
                    }
                }
            }
        }
    }

    if (state.showMonthSelector) {
        MonthSelector(
            selectedMonth = state.selectedMonth,
            onMonthSelected = { viewModel.onMonthSelected(it) }
        )
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
