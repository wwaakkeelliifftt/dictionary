package com.example.dictionary.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.use_case.WordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val useCase: WordUseCases
): ViewModel() {

    private val _searchQuery = mutableStateOf<String>("")
    val searchQuery: State<String> get() = _searchQuery

    private val _wordInfoState = mutableStateOf<WordInfoState>(WordInfoState())
    val wordInfoState: State<WordInfoState> get() = _wordInfoState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500L)

            useCase.getWordInfo.invoke(query).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _wordInfoState.value = _wordInfoState.value.copy(
                            wordInfoItems = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _wordInfoState.value = _wordInfoState.value.copy(
                            wordInfoItems = result.data ?: emptyList(),
                            isLoading = false
                        )
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = result.message ?: "Unknown error"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _wordInfoState.value = _wordInfoState.value.copy(
                            wordInfoItems = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)

        }
    }


    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
    }

}
