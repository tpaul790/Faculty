package com.ppdm.app.data.repository

import androidx.work.*
import com.ppdm.app.data.local.*
import com.ppdm.app.data.remote.*
import com.ppdm.app.worker.SyncWorker // Correct Package
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ProjectDao,
    private val workManager: WorkManager
) {

    suspend fun getProjects(
        userId: Long,
        page: Int,
        search: String,
        active: Boolean,
        isOnline: Boolean
    ): List<ProjectEntity> {

        // 1. Fetch from Server if Online
        if (isOnline) {
            try {
                val nameQuery = search.ifEmpty { null }
                val activeQuery = if (active) true else null

                val request = ProjectPageRequest(page, 10, nameQuery, activeQuery)
                val response = api.getUserProjects(userId, request)

                if (response.isSuccessful && response.body() != null) {
                    val entities = response.body()!!.content.map { dto ->
                        ProjectEntity(dto.id, dto.name, dto.budget, dto.active, dto.officeLatitude, dto.officeLongitude)
                    }
                    if (page == 0) dao.clearAll()
                    dao.insertAll(entities)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 2. Load Local Data (Server Synced)
        val syncedProjects = dao.getProjects(search, active)

        // 3. Load Offline Data (Not Synced yet)
        val offlineRaw = dao.getOfflineProjects()

        val offlineFiltered = offlineRaw.filter { proj ->
            (search.isEmpty() || proj.name.contains(search, ignoreCase = true)) &&
                    (!active || proj.active)
        }

        // 4. Convert Offline to Entity (Use NEGATIVE IDs to avoid collision and allow animations)
        val offlineAsEntities = offlineFiltered.map { offline ->
            ProjectEntity(
                id = 0, // <--- CRITICAL CHANGE: Negative ID
                name = offline.name,
                budget = offline.budget,
                active = offline.active,
                officeLatitude = offline.officeLatitude,
                officeLongitude = offline.officeLongitude
            )
        }

        // 5. Return Merged List
        return offlineAsEntities + syncedProjects
    }

    suspend fun createProject(dto: ProjectCreateDto, isOnline: Boolean): Boolean {
        try {
            if (isOnline) {
                val res = api.createProject(dto)
                if (res.isSuccessful && res.body() != null) {
                    val result = res.body()!!
                    val entity = ProjectEntity(
                        result.id, result.name, result.budget,
                        result.active, result.officeLatitude, result.officeLongitude
                    )
                    dao.insertAll(listOf(entity))
                    return true
                }
            } else {
                // Offline Logic
                dao.insertOffline(
                    OfflineProjectEntity(
                        name = dto.name,
                        budget = dto.budget,
                        active = dto.active,
                        officeLatitude = dto.officeLatitude,
                        officeLongitude = dto.officeLongitude
                    )
                )
                scheduleWorker()
                return true
            }
        } catch (e: Exception) {
            // Fallback to offline if API call fails even if isOnline was true
            dao.insertOffline(OfflineProjectEntity(name = dto.name, budget = dto.budget, active = dto.active, officeLatitude = dto.officeLatitude, officeLongitude = dto.officeLongitude))
            scheduleWorker()
            return true
        }
        return false
    }

    suspend fun syncOfflineCreatedProjects(): Boolean {
        val offlineProjects = dao.getOfflineProjects()
        if (offlineProjects.isEmpty()) return false

        var changesMade = false

        offlineProjects.forEach { offline ->
            try {
                val response = api.createProject(ProjectCreateDto(offline.name, offline.budget, offline.active, offline.officeLatitude, offline.officeLongitude))

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    // Remove from offline DB
                    dao.deleteOffline(offline)

                    // Add to main DB (with real ID)
                    val entity = ProjectEntity(
                        result.id, result.name, result.budget,
                        result.active, result.officeLatitude, result.officeLongitude
                    )
                    dao.insertAll(listOf(entity))

                    changesMade = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return changesMade
    }

    private fun scheduleWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "sync_projects_work",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }

    suspend fun updateProject(id: Long, dto: ProjectUpdateDto, isOnline: Boolean): Boolean {
        try {
            if (isOnline) {
                val res = api.updateProject(id, dto)
                if (res.isSuccessful && res.body() != null) {
                    val result = res.body()!!
                    val entity = ProjectEntity(
                        result.id, result.name, result.budget,
                        result.active, result.officeLatitude, result.officeLongitude
                    )
                    dao.insertAll(listOf(entity))
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    suspend fun getProjectById(id: Long): ProjectEntity? {
        return dao.getProjectById(id)
    }
}