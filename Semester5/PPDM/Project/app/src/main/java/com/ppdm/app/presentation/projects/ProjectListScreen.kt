package com.ppdm.app.presentation.projects

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.ppdm.app.util.ConnectivityStatus
import com.ppdm.app.util.ShakeDetector
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProjectListScreen(
    navController: NavController,
    viewModel: ProjectListViewModel = hiltViewModel(),
    shakeDetector: ShakeDetector
) {
    val networkStatus by viewModel.networkStatus.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val onlyActive by viewModel.onlyActive.collectAsState()
    val username by viewModel.username.collectAsState()
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    val context = LocalContext.current

    val listState = rememberLazyListState()

    DisposableEffect(Unit) {
        shakeDetector.start {
            viewModel.onSearch("")
            if (viewModel.onlyActive.value) viewModel.toggleActive()

            Toast.makeText(context, "Device Shaken! Filters Cleared.", Toast.LENGTH_SHORT).show()
        }

        onDispose {
            shakeDetector.stop()
        }
    }

    // 1. Navigation on Logout
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            navController.navigate("login") { popUpTo(0) { inclusive = true } }
        }
    }

    // 2. Lifecycle Observer (Refresh on Resume)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // 3. Infinite Scroll Trigger
    val isAtBottom by remember {
        derivedStateOf {
            val total = listState.layoutInfo.totalItemsCount
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            total > 0 && last >= total - 2
        }
    }
    LaunchedEffect(isAtBottom) { if (isAtBottom) viewModel.loadNextPage() }

    // 4. ANIMATION: Pulsing Alpha for Offline Banner
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // 5. ANIMATION: Collapse FAB on Scroll
    val isFabExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$username's Projects", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.Default.ExitToApp, "Logout", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("project_create") },
                icon = { Icon(Icons.Default.Add, "Add") },
                text = { Text("New Project") },
                expanded = isFabExpanded, // Animates width
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // --- OFFLINE BANNER (Pulsing) ---
            AnimatedVisibility(
                visible = networkStatus != ConnectivityStatus.Available,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    color = Color(0xFFB00020).copy(alpha = pulseAlpha),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "You are Offline. Changes saved locally.",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // --- HEADER ---
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { viewModel.onSearch(it) },
                        placeholder = { Text("Search projects...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = CircleShape,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilterChip(
                            selected = onlyActive,
                            onClick = { viewModel.toggleActive() },
                            label = { Text("Active Only") },
                            leadingIcon = if (onlyActive) { { Icon(Icons.Outlined.Check, null) } } else null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- LIST ---
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = projects,
                    key = { it.id } // KEY IS REQUIRED FOR ANIMATIONS
                ) { project ->

                    // ANIMATION: Item Placement
                    Box(modifier = Modifier.animateItemPlacement(tween(500))) {
                        ProjectItemCard(
                            name = project.name,
                            budget = project.budget,
                            isActive = project.active,
                            isOffline = project.id < 0, // Negative ID means offline
                            onClick = { navController.navigate("project_detail/${project.id}") }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectItemCard(
    name: String,
    budget: Long,
    isActive: Boolean,
    isOffline: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().alpha(if(isOffline) 0.8f else 1f), // Slight dim for offline
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if(isOffline) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("(Unsynced)", color = Color.Red, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Text(
                    text = "Budget: ${NumberFormat.getCurrencyInstance(Locale.US).format(budget)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StatusBadge(isActive)
        }
    }
}

@Composable
fun StatusBadge(isActive: Boolean) {
    val (bgColor, textColor, text) = if (isActive) {
        Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Active")
    } else {
        Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Inactive")
    }
    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}