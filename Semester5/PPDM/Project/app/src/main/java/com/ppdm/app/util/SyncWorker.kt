package com.ppdm.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ppdm.app.data.repository.ProjectRepository
import com.ppdm.app.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ProjectRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val changesMade = repository.syncOfflineCreatedProjects()

            if (changesMade) {
                notificationHelper.showSyncNotification(1)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}