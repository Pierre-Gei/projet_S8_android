package fr.isen.geiguer.isensmartcompanion.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import fr.isen.geiguer.isensmartcompanion.services.JsonService
import fr.isen.geiguer.isensmartcompanion.services.UserPreferencesService

class AgendaView {
    @Composable
    fun AgendaScreen(padding: Modifier) {
        val context = LocalContext.current
        val courses = remember { mutableStateOf<List<EventModel>>(emptyList()) }
        val userEvents = remember { mutableStateOf<List<EventModel>>(emptyList()) }

        LaunchedEffect(Unit) {
            val allEvents = JsonService().getEvents(context)
            UserPreferencesService().getUserEvents(context) { events ->
                userEvents.value = events
            }
        }

        Scaffold { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(courses.value + userEvents.value) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Title: ${event.title}")
                            Text(text = "Date: ${event.date}")
                            Text(text = "Location: ${event.location}")
                            Text(text = "Description: ${event.description}")
                        }
                    }
                }
            }
        }
    }
}