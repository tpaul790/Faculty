package com.ppdm.app.data.local

import androidx.room.*

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val budget: Long,
    val active: Boolean,
    val officeLatitude: String?,
    val officeLongitude: String?
)

@Entity(tableName = "offline_projects")
data class OfflineProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val budget: Long,
    val active: Boolean,
    val officeLatitude: String?,
    val officeLongitude: String?
)

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Query("SELECT * FROM projects WHERE name LIKE '%' || :search || '%' AND (:onlyActive = 0 OR active = 1)")
    suspend fun getProjects(search: String, onlyActive: Boolean): List<ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<ProjectEntity>)

    @Query("DELETE FROM projects")
    suspend fun clearAll()

    // Offline Queue
    @Insert
    suspend fun insertOffline(project: OfflineProjectEntity)

    @Query("SELECT * FROM offline_projects")
    suspend fun getOfflineProjects(): List<OfflineProjectEntity>

    @Delete
    suspend fun deleteOffline(project: OfflineProjectEntity)
}

@Database(entities = [ProjectEntity::class, OfflineProjectEntity::class], version = 3)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}