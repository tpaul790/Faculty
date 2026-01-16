package com.ppdm.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppdm.app.data.local.TokenManager
import com.ppdm.app.data.remote.ApiService
import com.ppdm.app.data.remote.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(val isLoading: Boolean = false, val error: String? = null, val success: Boolean = false)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun login(u: String, p: String) {
        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)
            try {
                val res = api.login(LoginRequest(u, p))
                if (res.isSuccessful && res.body() != null) {
                    val body = res.body()!!
                    tokenManager.saveAuthDetails(body.token, body.id, body.username)
                    _state.value = LoginState(success = true)
                } else {
                    _state.value = LoginState(error = "Login Failed: ${res.code()}")
                }
            } catch (e: Exception) {
                _state.value = LoginState(error = e.message)
            }
        }
    }
}