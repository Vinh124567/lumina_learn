package com.example.luminalearn.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Class BaseViewModel quản lý luồng dữ liệu 1 chiều (Unidirectional Data Flow) chuẩn MVI.
 *
 * @param State Kiểu dữ liệu trạng thái UI (kế thừa [UiState])
 * @param Intent Kiểu dữ liệu hành động người dùng (kế thừa [UiIntent])
 * @param Effect Kiểu dữ liệu sự kiện 1 lần (kế thừa [UiEffect])
 */
abstract class BaseViewModel<State : UiState, Intent : UiIntent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _uiEffect = Channel<Effect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    /**
     * Lấy trạng thái UI hiện tại.
     */
    protected val currentState: State
        get() = _uiState.value

    /**
     * Nhận Intent gửi từ View và kích hoạt xử lý.
     */
    fun processIntent(intent: Intent) {
        handleIntent(intent)
    }

    /**
     * Xử lý từng Intent theo logic nghiệp vụ của ViewModel con.
     */
    protected abstract fun handleIntent(intent: Intent)

    /**
     * Cập nhật UiState an toàn (thread-safe).
     */
    protected fun setState(reducer: State.() -> State) {
        _uiState.update(reducer)
    }

    /**
     * Gửi Side-Effect ra ngoài UI.
     */
    protected fun setEffect(effect: Effect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
