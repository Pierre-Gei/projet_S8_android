package fr.isen.geiguer.isensmartcompanion.models

import java.io.Serializable

data class EventModel(
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable