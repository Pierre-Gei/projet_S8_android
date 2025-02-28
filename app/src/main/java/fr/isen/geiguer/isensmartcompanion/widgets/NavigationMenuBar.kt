package fr.isen.geiguer.isensmartcompanion.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

class NavigationMenuBar {
    @Composable
    fun BottomNavBar(navController: NavController) {
        val navItems = listOf("Home", "Events", "History")
        NavigationBar {
            navItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            when (item) {
                                "Home" -> Icons.Rounded.Home
                                "Events" -> Icons.Rounded.DateRange
                                "History" -> Icons.Rounded.Menu
                                else -> Icons.Rounded.Clear
                            },
                            contentDescription = item
                        )
                    },
                    label = { Text(item) },
                    selected = false,
                    onClick = {
                        when (item) {
                            "Home" -> navController.navigate("main")
                            "Events" -> navController.navigate("events")
                            "History" -> navController.navigate("history")
                            else -> {}
                        }
                    },
                )
            }
        }
    }
}