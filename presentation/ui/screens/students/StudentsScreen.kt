package com.alborihi.myapp.presentation.ui.screens.students

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
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    onNavigateToStudentProfile: (Long) -> Unit,
    viewModel: StudentsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "إدارة الطلاب",
                showAddButton = true,
                onAddClick = { viewModel.openAddForm() }
            )
        },
        floatingActionButton = {
            FAB(
                onClick = { viewModel.openAddForm() },
                icon = Icons.Default.Add,
                contentDescription = "إضافة طالب"
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(message = state.error!!) { viewModel.loadStudents() }
            state.students.isEmpty() -> EmptyState(message = "لا يوجد طلاب. أضف طالباً جديداً.")
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "بحث"
                            )
                        },
                        placeholder = { Text("ابحث عن طالب...") },
                        singleLine = true
                    )

                    // Students List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.filteredStudents.ifEmpty { state.students },
                            key = { it.id }
                        ) { student ->
                            StudentItem(
                                student = student,
                                onClick = { onNavigateToStudentProfile(student.id) },
                                onEditClick = { viewModel.openEditForm(student) },
                                onDeleteClick = { /* Show delete confirmation */ }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add/Edit Form
    if (state.isFormVisible) {
        StudentFormScreen(
            student = state.editingStudent,
            onDismiss = { viewModel.closeForm() },
            onSave = { student -> viewModel.saveStudent(student) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentItem(
    student: Student,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleAvatar(
                modifier = Modifier.size(48.dp),
                label = student.name.take(1).uppercase()
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${student.className} ${student.classSection} | ${student.parentPhone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "تعديل"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    student: Student?,
    onDismiss: () -> Unit,
    onSave: (Student) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<StudentFormViewModel>()

    LaunchedEffect(student) {
        student?.let(viewModel::initializeWithStudent)
    }

    val formState by viewModel.state.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (student != null) "تعديل طالب" else "إضافة طالب جديد",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = formState.name,
                onValueChange = viewModel::onNameChange,
                label = "الاسم",
                isError = formState.validationErrors.containsKey("name"),
                errorMessage = formState.validationErrors["name"]
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = formState.className,
                onValueChange = viewModel::onClassNameChange,
                label = "الفصل",
                isError = formState.validationErrors.containsKey("className"),
                errorMessage = formState.validationErrors["className"]
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = formState.classSection,
                onValueChange = viewModel::onClassSectionChange,
                label = "القسم"
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = formState.parentName,
                onValueChange = viewModel::onParentNameChange,
                label = "اسم ولي الأمر"
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = formState.parentPhone,
                onValueChange = viewModel::onParentPhoneChange,
                label = "هاتف ولي الأمر",
                keyboardType = androidx.compose.foundation.text.KeyboardType.Phone,
                isError = formState.validationErrors.containsKey("parentPhone"),
                errorMessage = formState.validationErrors["parentPhone"]
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = formState.address,
                onValueChange = viewModel::onAddressChange,
                label = "العنوان"
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = formState.notes,
                onValueChange = viewModel::onNotesChange,
                label = "ملاحظات",
                singleLine = false
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomButton(
                    text = "إلغاء",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    isError = true
                )
                CustomButton(
                    text = if (student != null) "تحديث" else "إضافة",
                    onClick = { onSave(
                        Student(
                            id = formState.student?.id ?: 0,
                            name = formState.name,
                            className = formState.className,
                            classSection = formState.classSection,
                            parentName = formState.parentName,
                            parentPhone = formState.parentPhone,
                            address = formState.address,
                            notes = formState.notes
                        )
                    ) },
                    modifier = Modifier.weight(1f),
                    isLoading = formState.isSubmitting
                )
            }
        }
    }
}

@Composable
fun FAB(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        },
        text = { Text(contentDescription) }
    )
}
