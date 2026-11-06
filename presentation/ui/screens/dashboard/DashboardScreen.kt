package com.alborihi.myapp.presentation.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alborihi.myapp.presentation.ui.components.*
import com.alborihi.myapp.presentation.ui.util.Resource
import com.alborihi.myapp.presentation.ui.util.formatPercentage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToStudents: () -> Unit,
    onNavigateToGrades: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToImport: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToBackup: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardStats()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "لوحة التحكم",
                showBackButton = false
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(message = state.error!!) { viewModel.loadDashboardStats() }
            state.stats == null -> EmptyState(message = "لا توجد بيانات لعرضها")
            else -> {
                val stats = state.stats!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        StatCard(
                            title = "إجمالي الطلاب",
                            value = stats.totalStudents.toString(),
                            icon = androidx.compose.material.icons.Icons.Default.Person
                        )
                    }
                    item {
                        StatCard(
                            title = "الطلاب النشطون",
                            value = stats.activeStudents.toString(),
                            icon = androidx.compose.material.icons.Icons.Default.PersonCheck
                        )
                    }
                    item {
                        StatCard(
                            title = "إجمالي الدرجات",
                            value = stats.totalGrades.toString(),
                            icon = androidx.compose.material.icons.Icons.Default.Star
                        )
                    }
                    item {
                        StatCard(
                            title = "متوسط النسبة",
                            value = stats.averagePercentage.formatPercentage(),
                            icon = androidx.compose.material.icons.Icons.Default.TrendingUp
                        )
                    }

                    item {
                        Text(
                            text = "أفضل الطلاب",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(stats.topStudents) { student ->
                        StudentChip(student = student, onClick = {})
                    }

                    if (stats.topStudents.isEmpty()) {
                        item {
                            EmptyState(message = "لا يوجد طلاب بعد")
                        }
                    }
                }
            }
        }
    }

    state.snackbarMessage?.let { message ->
        Snackbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onDismissRequest = { viewModel.dismissSnackbar() }
        ) {
            Text(text = message)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
