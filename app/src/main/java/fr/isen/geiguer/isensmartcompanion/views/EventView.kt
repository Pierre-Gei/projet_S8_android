package fr.isen.geiguer.isensmartcompanion.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.geiguer.isensmartcompanion.activities.EventDetailActivity
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import fr.isen.geiguer.isensmartcompanion.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback

class EventView {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventsScreen(modifier: Modifier) {
        val context = LocalContext.current
        val events = remember { mutableStateOf<List<EventModel>>(emptyList()) }

        LaunchedEffect(Unit) {
            val call = RetrofitInstance.api.getEvents()
            call.enqueue(object : Callback<List<EventModel>> {
                override fun onResponse(
                    call: Call<List<EventModel>>,
                    response: retrofit2.Response<List<EventModel>>
                ) {
                    if (response.isSuccessful) {
                        events.value = response.body() ?: emptyList()
                    } else {
                        Log.e(TAG, "Failed to fetch events: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                    Log.e(TAG, "Failed to fetch events", t)
                }
            })
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Event") },
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn {
                        items(events.value) { event ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable(onClick = {
                                        val intent =
                                            Intent(context, EventDetailActivity::class.java).apply {
                                                putExtra("event", event)
                                            }
                                        context.startActivity(intent)
                                    })
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = event.title)
                                    Text(text = event.description)
                                    Text(text = event.date)
                                    Text(text = event.location)
                                    Text(text = event.category)
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}