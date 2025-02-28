package fr.isen.geiguer.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import fr.isen.geiguer.isensmartcompanion.services.roomDB.AppDatabase
import fr.isen.geiguer.isensmartcompanion.services.roomDB.Interaction
import fr.isen.geiguer.isensmartcompanion.services.roomDB.InteractionDao
import fr.isen.geiguer.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.geiguer.isensmartcompanion.views.EventView
import fr.isen.geiguer.isensmartcompanion.views.HistoryView
import fr.isen.geiguer.isensmartcompanion.views.MainPageView
import fr.isen.geiguer.isensmartcompanion.widgets.NavigationMenuBar
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var interactionDao: InteractionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)
        interactionDao = db.interactionDao()
        setContent {
            val navController = rememberNavController()
            ISENSmartCompanionTheme {
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
                                Modifier.padding(innerPadding),
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
                    }
                }
            }
        }
    }

    fun saveInteraction(question: String, answer: String) {
        val interaction = Interaction(question = question, answer = answer, date = Date())
        lifecycleScope.launch {
            interactionDao.insert(interaction)
        }
    }
}