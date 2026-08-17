package com.example.luminalearn.presentation.main

import com.example.luminalearn.core.base.BaseViewModel

/**
 * MainViewModel triển khai logic xử lý MVI cho màn hình Main mẫu.
 */
class MainViewModel : BaseViewModel<MainUiState, MainUiIntent, MainUiEffect>(
    initialState = MainUiState()
) {

    override fun handleIntent(intent: MainUiIntent) {
        when (intent) {
            is MainUiIntent.IncrementCounter -> {
                setState { copy(counter = counter + 1) }
            }
            is MainUiIntent.DecrementCounter -> {
                setState { copy(counter = counter - 1) }
            }
            is MainUiIntent.ResetCounter -> {
                setState { copy(counter = 0) }
            }
            is MainUiIntent.ShowToastRequested -> {
                setEffect(MainUiEffect.ShowToast("Giá trị counter hiện tại là: ${currentState.counter}"))
            }
        }
    }
}
