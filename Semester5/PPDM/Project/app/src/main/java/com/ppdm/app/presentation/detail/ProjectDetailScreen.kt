package com.ppdm.app.presentation.detail

import android.widget.Toast // Import Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Import Context
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Long?,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current // 1. Get Context for Toast

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
    }

    // --- 2. SUCCESS NOTIFICATION ---
    LaunchedEffect(state.success) {
        if (state.success) {
            val message = if (projectId == null) "Project Created Successfully!" else "Project Updated Successfully!"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            navController.popBackStack()
        }
    }

    // --- 3. ERROR NOTIFICATION ---
    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
        }
    }

    // Update map camera if we have a location
    LaunchedEffect(state.location) {
        state.location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(if (projectId == null) "Create Project" else "Edit Project") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Name Input
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Budget Input
            OutlinedTextField(
                value = state.budget,
                onValueChange = { viewModel.onBudgetChange(it) },
                label = { Text("Budget") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Active Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.active,
                    onCheckedChange = { viewModel.onActiveChange(it) }
                )
                Text(text = "Project is Active")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Office Location (Tap on map to select)", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Google Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        viewModel.setOfficeLocation(latLng)
                    }
                ) {
                    state.location?.let { loc ->
                        Marker(
                            state = MarkerState(position = loc),
                            title = "Office Location"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            if(state.location != null) {
                Text("Selected: ${state.location!!.latitude}, ${state.location!!.longitude}")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = { viewModel.submit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Project")
            }
        }
    }
}