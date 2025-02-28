package fr.isen.geiguer.isensmartcompanion.services

import android.content.Context
import androidx.lifecycle.lifecycleScope
import fr.isen.geiguer.isensmartcompanion.services.roomDB.AppDatabase
import fr.isen.geiguer.isensmartcompanion.services.roomDB.Interaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

class DatabaseService(private val context: Context, private val coroutineScope: CoroutineScope) {
    private val interactionDao = AppDatabase.getDatabase(context).interactionDao()

    fun saveInteraction(question: String, answer: String) {
        val interaction = Interaction(question = question, answer = answer, date = Date())
        coroutineScope.launch {
            interactionDao.insert(interaction)
        }
    }
}