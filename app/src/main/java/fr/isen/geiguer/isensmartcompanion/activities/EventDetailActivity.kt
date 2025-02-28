package fr.isen.geiguer.isensmartcompanion.activities

import EventDetailView
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import fr.isen.geiguer.isensmartcompanion.ui.theme.ISENSmartCompanionTheme


class EventDetailActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISENSmartCompanionTheme {
                val event = intent.getSerializableExtra("event") as? EventModel
                event?.let { eventModel ->
                    EventDetailView().EventDetailScreen(event = eventModel)
                }
            }
        }
    }
}