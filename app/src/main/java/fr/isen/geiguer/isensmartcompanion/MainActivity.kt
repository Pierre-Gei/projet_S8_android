package fr.isen.geiguer.isensmartcompanion

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.geiguer.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import retrofit2.Call
import retrofit2.Callback

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainPage(contentPadding: PaddingValues, navController: NavController) {
    val textFieldValue = remember { mutableStateOf("") }
    val inputHistory = remember { mutableStateOf(listOf<String>()) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(
                bottom = 74.dp,
                top = 32.dp
            ) // Adjust the bottom padding to avoid overlap with BottomNavBar
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.la_mere_patriev3), // Ensure this is a PNG, JPG, or WEBP file
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp),
            )
            Text(
                text = "ISEN Smart Companion",
            )
            inputHistory.value.forEach { input ->
                Text(text = input, modifier = Modifier.padding(top = 16.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                modifier = Modifier.padding(bottom = 32.dp),
                value = textFieldValue.value,
                onValueChange = { newValue -> textFieldValue.value = newValue },
                label = { Text("Enter your question") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            Log.i(TAG, "TextField content: ${textFieldValue.value}")
                            makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                            inputHistory.value += textFieldValue.value
                            textFieldValue.value = ""
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "Next",
                        )
                    }
                }
            )
        }
    }
}

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) {
        NavigationHost(navController = navController)
    }
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainPage(contentPadding = PaddingValues(), navController) }
        composable("events") { EventsScreen() }
        composable("history") { HistoryScreen() }
    }
}

@Composable
fun EventsScreen() {
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

    Column(
        modifier = Modifier.fillMaxSize(),
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
                            val intent = Intent(context, EventDetailActivity::class.java).apply {
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

@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "History Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ISENSmartCompanionTheme {
        val navController = rememberNavController()
        MainPage(contentPadding = PaddingValues(), navController = navController)
    }
}