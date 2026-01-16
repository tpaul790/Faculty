package com.ppdm.app.presentation.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppdm.app.data.local.ProjectEntity
import com.ppdm.app.data.local.TokenManager
import com.ppdm.app.data.remote.ApiService
import com.ppdm.app.data.remote.WebSocketManager
import com.ppdm.app.data.repository.ProjectRepository
import com.ppdm.app.util.ConnectivityStatus
import com.ppdm.app.util.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val repo: ProjectRepository,
    private val ws: WebSocketManager,
    private val tokenManager: TokenManager,
    private val api: ApiService,
    private val networkObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _projects = MutableStateFlow<List<ProjectEntity>>(emptyList())
    val projects = _projects.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut = _isLoggedOut.asStateFlow()

    private var currentUserId: Long? = null

    private val _username = MutableStateFlow("Me")
    val username = _username.asStateFlow()

    val searchText = MutableStateFlow("")
    val onlyActive = MutableStateFlow(false)
    private var currentPage = 0

    private val _networkStatus = MutableStateFlow(ConnectivityStatus.Unavailable)
    val networkStatus = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            networkObserver.observe().collect { status ->
                _networkStatus.value = status

                if (status == ConnectivityStatus.Available) {
                    val synced = repo.syncOfflineCreatedProjects()

                    if (synced) {
                        refresh()
                    } else {
                        refresh()
                    }
                }
            }
        }

        ws.connect()

        viewModelScope.launch {
            tokenManager.userId.collect { id ->
                if (id != null) {
                    currentUserId = id
                    refresh()
                }
            }
        }

        viewModelScope.launch {
            tokenManager.username.collect { name ->
                if (name != null) _username.value = name
            }
        }

        viewModelScope.launch {
            ws.events.collect { refresh() }
        }

        viewModelScope.launch { repo.syncOfflineCreatedProjects() }
    }

    fun refresh() {
        currentPage = 0
        loadProjects()
    }

    fun onSearch(query: String) {
        searchText.value = query
        refresh()
    }

    fun toggleActive() {
        onlyActive.value = !onlyActive.value
        refresh()
    }

    fun loadNextPage() {
        currentPage++
        loadProjects()
    }

    private fun loadProjects() {
        val userId = currentUserId ?: return

        val isOnline = _networkStatus.value == ConnectivityStatus.Available

        viewModelScope.launch {
            val result = repo.getProjects(
                userId,
                currentPage,
                searchText.value,
                onlyActive.value,
                isOnline
            )

            if (currentPage == 0) {
                _projects.value = result
            } else {
                val currentIds = _projects.value.map { it.id }.toSet()
                val newUniqueItems = result.filter { it.id !in currentIds }
                _projects.value += newUniqueItems
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                if (currentUserId != null) {
                    api.logout(currentUserId!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                tokenManager.clearAuthDetails()
                _isLoggedOut.value = true
            }
        }
    }
}


