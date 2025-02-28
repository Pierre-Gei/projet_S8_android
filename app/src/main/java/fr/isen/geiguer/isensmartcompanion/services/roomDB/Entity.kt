package fr.isen.geiguer.isensmartcompanion.services.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "interaction")
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val date: Date
)