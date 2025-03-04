import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import fr.isen.geiguer.isensmartcompanion.services.NotificationsService

class EventDetailView {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun EventDetailScreen(event: EventModel) {
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("notified_events", Context.MODE_PRIVATE)
        val isSubscribed = remember { mutableStateOf(sharedPreferences.contains(event.title)) }

        Scaffold (
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Event detail") },
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                ) {
                                    Icon(Icons.Rounded.Info, contentDescription = "Event")
                                    Text(text = event.title)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                ) {
                                    Icon(Icons.Rounded.DateRange, contentDescription = "Date")
                                    Text(text = event.date)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                ) {
                                    Icon(Icons.Rounded.LocationOn, contentDescription = "Location")
                                    Text(text = event.location)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                ) {
                                    Icon(Icons.Rounded.Menu, contentDescription = "Description")
                                    Text(text = event.description)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        if (isSubscribed.value) {
                                            sharedPreferences.edit().remove(event.title).apply()
                                            makeText(
                                                context,
                                                "You are now unsubscribed to this event",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            sharedPreferences.edit().putBoolean(event.title, true)
                                                .apply()
                                            val notificationsService = NotificationsService()
                                            notificationsService.scheduleNotification(context, event)
                                            makeText(
                                                context,
                                                "You are now subscribed to this event",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        isSubscribed.value = !isSubscribed.value
                                        Log.d("sharedPreferences", sharedPreferences.all.toString())
                                    }
                                ) {
                                    //update the icon based on the subscription status dynamically
                                    Icon(
                                        imageVector = if (isSubscribed.value) Icons.Filled.Close else Icons.Filled.Notifications,
                                        contentDescription = "Subscribe"
                                    )
                                    Text(text = if (isSubscribed.value) "Unsubscribe" else "Subscribe")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}