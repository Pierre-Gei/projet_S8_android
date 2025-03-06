package fr.isen.geiguer.isensmartcompanion.services

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPreferencesService {
    fun getUserEvents(context: Context, callback: (List<EventModel>) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("notified_events", Context.MODE_PRIVATE)
        val call = RetrofitInstance.api.getEvents()
        call.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(call: Call<List<EventModel>>, response: Response<List<EventModel>>) {
                if (response.isSuccessful) {
                    val allEvents = response.body() ?: emptyList()
                    val userEvents = allEvents.filter { sharedPreferences.contains(it.title) }
                    callback(userEvents)
                } else {
                    Log.e(TAG, "Failed to fetch events: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch events", t)
                callback(emptyList())
            }
        })
    }
}