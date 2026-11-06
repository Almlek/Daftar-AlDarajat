package com.alborihi.myapp.presentation.ui.screens.dashboard

import com.alborihi.myapp.domain.model.DashboardStats

data class DashboardState(
    val stats: DashboardStats? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
)
