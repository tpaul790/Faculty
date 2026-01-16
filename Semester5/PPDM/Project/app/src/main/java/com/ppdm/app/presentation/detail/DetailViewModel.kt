package com.ppdm.app.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppdm.app.data.remote.ProjectCreateDto
import com.ppdm.app.data.remote.ProjectUpdateDto
import com.ppdm.app.data.repository.ProjectRepository
import com.ppdm.app.util.ConnectivityStatus
import com.ppdm.app.util.NetworkConnectivityObserver
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailState(
    val name: String = "",
    val budget: String = "",
    val active: Boolean = true,
    val location: LatLng? = null,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: ProjectRepository,
    savedStateHandle: SavedStateHandle,
    private val networkObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    private var isOnline = false

    private val projectId: Long? = savedStateHandle.get<Long>("id")?.let { if (it == -1L) null else it }

    init {
        viewModelScope.launch {
            networkObserver.observe().collect { status ->
                isOnline = (status == ConnectivityStatus.Available)
            }
        }

        if (projectId != null) loadProject(projectId)
    }

    private fun loadProject(id: Long) {
        viewModelScope.launch {
            val p = repo.getProjectById(id)
            if (p != null) {
                _state.value = _state.value.copy(
                    name = p.name,
                    budget = p.budget.toString(),
                    active = p.active,
                    location = if (p.officeLatitude != null && p.officeLongitude != null)
                        LatLng(p.officeLatitude.toDouble(), p.officeLongitude.toDouble())
                    else null
                )
            }
        }
    }

    fun onNameChange(v: String) { _state.value = _state.value.copy(name = v) }

    fun onBudgetChange(v: String) {
        if (v.all { it.isDigit() }) _state.value = _state.value.copy(budget = v)
    }

    fun onActiveChange(v: Boolean) { _state.value = _state.value.copy(active = v) }

    fun setOfficeLocation(l: LatLng) { _state.value = _state.value.copy(location = l) }

    fun submit() {
        viewModelScope.launch {
            val budgetLong = _state.value.budget.toLongOrNull() ?: 0L

            if (projectId == null) {
                val res = repo.createProject(
                    ProjectCreateDto(
                        name = _state.value.name,
                        budget = budgetLong,
                        active = _state.value.active,
                        officeLatitude = _state.value.location?.latitude?.toString(),
                        officeLongitude = _state.value.location?.longitude?.toString()
                    ),
                    isOnline = isOnline
                )
                if (res) _state.value = _state.value.copy(success = true)
            } else {
                val res = repo.updateProject(
                    projectId,
                    ProjectUpdateDto(
                        name = _state.value.name,
                        budget = budgetLong,
                        active = _state.value.active,
                        officeLatitude = _state.value.location?.latitude?.toString(),
                        officeLongitude = _state.value.location?.longitude?.toString()
                    ),
                    isOnline = isOnline
                )
                if (res) _state.value = _state.value.copy(success = true)
            }
        }
    }
}