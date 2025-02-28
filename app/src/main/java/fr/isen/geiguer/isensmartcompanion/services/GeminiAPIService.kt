package fr.isen.geiguer.isensmartcompanion.services

import android.content.ContentValues.TAG
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.geiguer.isensmartcompanion.BuildConfig
import kotlinx.serialization.SerializationException

class GeminiAPIService {
    suspend fun AskGeminAI(question: String): String {
        return try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = BuildConfig.API_KEY
            )
            val response = generativeModel.generateContent(question)
            response.text.toString()
        } catch (e: SerializationException) {
            Log.e(TAG, "Serialization error: ${e.message}", e)
            "Error: Unable to process the response from the server."
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}", e)
            "Error: An unexpected error occurred."
        }
    }

}