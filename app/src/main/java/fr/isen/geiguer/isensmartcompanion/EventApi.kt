package fr.isen.geiguer.isensmartcompanion

import retrofit2.Call
import retrofit2.http.GET

interface EventApi {
    @GET("events.json")
    fun getEvents(): Call<List<EventModel>>
}
