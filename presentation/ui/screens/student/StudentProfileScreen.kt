package com.alborihi.myapp.presentation.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    studentId: Long,
    viewModel: StudentProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(studentId) {
        viewModel.loadStudentProfile(studentId)
    }

    Scaffold(
        topBar = {
