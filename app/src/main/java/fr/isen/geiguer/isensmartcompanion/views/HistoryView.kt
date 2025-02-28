package fr.isen.geiguer.isensmartcompanion.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.geiguer.isensmartcompanion.services.roomDB.AppDatabase
import fr.isen.geiguer.isensmartcompanion.services.roomDB.Interaction
import kotlinx.coroutines.launch

class HistoryView {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HistoryScreen(modifier: Modifier) {
        val context = LocalContext.current
        val interactionDao = AppDatabase.getDatabase(context).interactionDao()
        val interactions = remember { mutableStateOf<List<Interaction>>(emptyList()) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            interactions.value = interactionDao.getAllInteractions()
        }

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text("History") },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            interactionDao.deleteAll()
                            interactions.value = interactionDao.getAllInteractions()
                        }
                    }
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Clear")
                }
            },
            content = { paddingValues ->
                LazyColumn(
                    contentPadding = paddingValues
                ) {
                    items(interactions.value) { interaction ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Question: ${interaction.question}")
                                Text(text = "Answer: ${interaction.answer}")
                                Text(text = "Date: ${interaction.date}")
                                Button(onClick = {
                                    coroutineScope.launch {
                                        interactionDao.delete(interaction)
                                        interactions.value = interactionDao.getAllInteractions()
                                    }
                                }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        )
    }

}