package fr.isen.geiguer.isensmartcompanion.services.roomDB

import fr.isen.geiguer.isensmartcompanion.models.EventModel
import retrofit2.Call
import retrofit2.http.GET

interface EventApi {
    @GET("events.json")
    fun getEvents(): Call<List<EventModel>>
}
