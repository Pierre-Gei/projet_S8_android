package fr.isen.geiguer.isensmartcompanion.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import fr.isen.geiguer.isensmartcompanion.services.NotificationsService

class EventDetailView {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun EventDetailScreen(event: EventModel) {
        Scaffold {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = event.title)
                    Text(text = event.date)
                    Text(text = event.location)
                    Text(text = event.category)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = event.description)
                    val context = LocalContext.current
                    Button(
                        onClick = { NotificationsService().scheduleNotification(context, event) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Subscribe to Notifications")
                    }
                }
            }
        }
    }
}