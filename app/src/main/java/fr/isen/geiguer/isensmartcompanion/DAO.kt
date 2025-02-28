package fr.isen.geiguer.isensmartcompanion

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InteractionDao {
    @Insert
    suspend fun insert(interaction: Interaction)

    @Query("SELECT * FROM interaction ORDER BY date DESC")
    suspend fun getAllInteractions(): List<Interaction>

    @Delete
    suspend fun delete(interaction: Interaction)

    @Query("DELETE FROM interaction")
    suspend fun deleteAll()
}