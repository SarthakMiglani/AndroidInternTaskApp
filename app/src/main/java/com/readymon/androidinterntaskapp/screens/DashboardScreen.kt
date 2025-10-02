package com.readymon.androidinterntaskapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Notifications", Icons.Default.Notifications),
        BottomNavItem("Settings", Icons.Default.Settings)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tabs[selectedTab].title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeFragment()
                1 -> NotificationsFragment()
                2 -> SettingsFragment()
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)