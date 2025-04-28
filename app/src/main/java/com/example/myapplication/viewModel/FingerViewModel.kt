package com.example.myapplication.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.GestureApplication
import com.example.myapplication.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FingerViewModel (
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FingerUiState())
    val uiState: StateFlow<FingerUiState> = _uiState.asStateFlow()

    val ip_Adress: StateFlow<String> =
        userPreferencesRepository.ip_Adress.map {
            it
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "0"
        )

    val mode = mutableStateOf(false)



    fun updateCheckbox(){
        mode.value = !mode.value
    }
    fun updateTextField(input: String, fingerNumber: Int) {
        val value = Integer.parseInt(input)
        if (value > 100) {
            inputer("100", fingerNumber)
        }
        else if (value<0){
            inputer("0",fingerNumber)
        }
        else inputer(input,fingerNumber)
    }

    fun inputer(input: String, fingerNumber: Int){
        when (fingerNumber){
            1->{_uiState.update {it.copy(fingerOne = Integer.parseInt(input))}}
            2->{_uiState.update {it.copy(fingerTwo = Integer.parseInt(input))}}
            3->{_uiState.update {it.copy(fingerThree = Integer.parseInt(input))}}
            4->{_uiState.update {it.copy(fingerFour = Integer.parseInt(input))}}
            5->{_uiState.update {it.copy(fingerFive = Integer.parseInt(input))}}
        }
    }

    fun pictureUiUpdate(uiStater: FingerUiState){
        _uiState.update { it.copy(
            fingerOne = uiStater.fingerOne,
            fingerTwo = uiStater.fingerTwo,
            fingerThree = uiStater.fingerThree,
            fingerFour = uiStater.fingerFour,
            fingerFive = uiStater.fingerFive
        ) }
        }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GestureApplication)
                FingerViewModel(application.userPreferencesRepository)
            }
        }
    }


    fun saveIp(ip: String){
        viewModelScope.launch {
            userPreferencesRepository.saveIp(ip)
        }
    }

    }

