package fr.isen.geiguer.isensmartcompanion.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class AgendaView {
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AgendaScreen(modifier: Modifier) {
        val context = LocalContext.current
        val courses = remember { mutableStateOf<List<EventModel>>(emptyList()) }
        val userEvents = remember { mutableStateOf<List<EventModel>>(emptyList()) }

        LaunchedEffect(Unit) {
            courses.value = JsonService().getEvents(context)
            UserPreferencesService().getUserEvents(context) { events ->
                userEvents.value = events
            }
        }

        val jsonDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val dbDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)

        val allEvents = (courses.value + userEvents.value)
            .sortedBy { event ->
                try {
                    LocalDate.parse(event.date, jsonDateFormatter)
                } catch (e: Exception) {
                    LocalDate.parse(event.date, dbDateFormatter)
                }
            }
            .groupBy { event ->
                try {
                    LocalDate.parse(event.date, jsonDateFormatter)
                } catch (e: Exception) {
                    LocalDate.parse(event.date, dbDateFormatter)
                }
            }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Agenda") },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                allEvents.forEach { (date, events) ->
                    item {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    items(events) { event ->
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
}