package fr.isen.geiguer.isensmartcompanion

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.geiguer.isensmartcompanion.services.DatabaseService
import fr.isen.geiguer.isensmartcompanion.services.NotificationsService
import fr.isen.geiguer.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.geiguer.isensmartcompanion.views.AgendaView
import fr.isen.geiguer.isensmartcompanion.views.EventView
import fr.isen.geiguer.isensmartcompanion.views.HistoryView
import fr.isen.geiguer.isensmartcompanion.views.MainPageView
import fr.isen.geiguer.isensmartcompanion.widgets.NavigationMenuBar

class MainActivity : ComponentActivity() {
    private lateinit var databaseService: DatabaseService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseService = DatabaseService(this, lifecycleScope)
        setContent {
            val navController = rememberNavController()
            ISENSmartCompanionTheme {
                NotificationsService().createNotificationChannel(this)
                enableEdgeToEdge()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    bottomBar = { NavigationMenuBar().BottomNavBar(navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = "main") {
                        composable("main") {
                            MainPageView().MainPage(
                                Modifier.padding(
                                    innerPadding
                                ),
                                navController
                            )
                        }
                        composable("events") {
                            EventView().EventsScreen(
                                Modifier.padding(
                                    innerPadding
                                )
                            )
                        }
                        composable("history") {
                            HistoryView().HistoryScreen(
                                Modifier.padding(
                                    innerPadding
                                )
                            )
                        }
                        composable("agenda") {
                            AgendaView().AgendaScreen(
                                Modifier.padding(
                                    innerPadding
                                )
                            )
                        }
                    }
                }
            }
        }
        openNotificationSettings()
    }
    private fun openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
            startActivity(intent)
        }
    }
}