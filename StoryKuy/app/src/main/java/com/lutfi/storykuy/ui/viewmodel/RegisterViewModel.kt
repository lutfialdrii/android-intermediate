package com.lutfi.storykuy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutfi.storykuy.data.AuthRepository
import com.lutfi.storykuy.data.ResultState
import com.lutfi.storykuy.data.models.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerState = MutableLiveData<ResultState<Unit>>()
    val registerState: LiveData<ResultState<Unit>> = _registerState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = ResultState.Loading
            try {
                val response: Response<RegisterResponse> =
                    authRepository.register(name, email, password)
                val responseBody : RegisterResponse? = response.body()
                if (responseBody?.error == true || responseBody == null) {
                    _registerState.value = ResultState.Error(responseBody?.message.toString())
                } else {
                    _registerState.value = ResultState.Success(Unit)
                }
            } catch (e: Exception) {
                _registerState.value = ResultState.Error(e.message.toString())
            }

        }
    }
}