package com.juchan.todo.ui.todo

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juchan.todo.data.Todo
import com.juchan.todo.data.TodoRepository
import kotlinx.coroutines.launch

// 로직 분리
class TodoViewModel : ViewModel() {

    private val repository = TodoRepository()

    // 실제 데이터: 이 클래스 안에서만 수정 가능(private)
    private val _todos = mutableStateListOf(
        Todo(1, "장보기"),
        Todo(2, "Kotlin 공부하기", isDone = true),
        Todo(3, "개발 일지 쓰기"),
    )
//    private val _todos = listOf(
//        Todo(1, "장보기"),
//        Todo(2, "Kotlin 공부하기", isDone = true),
//        Todo(3, "개발 일지 쓰기"),
//        Todo(4, "GitHub 커밋하기"),
//    ).toMutableStateList() // .toMutableStateList() 목록이 바뀌면 화면이 저절로 다시 그려짐

    // 바깥(화면)에는 읽기 전용으로만 공개
    val todos: List<Todo> get() = _todos

    // 네트워크 상태 (private set: 바깥에서는 읽기만 가능)
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // 통계용 계산 값
    val total: Int get() = _todos.size
    val doneCount: Int get() = _todos.count { it.isDone }
    val remainCount: Int get() = total - doneCount
    val progress: Float get() = if (total == 0) 0f else doneCount.toFloat() / total


    // 신규 등록
    fun add(title: String) {
        val newId = (_todos.maxOfOrNull { it.id } ?: 0) + 1
        _todos.add(0, Todo(id = newId, title = title))
    }

    fun toggle(id: Int, checked: Boolean) {
        val index = _todos.indexOfFirst { it.id == id }
        if (index != -1) _todos[index] = _todos[index].copy(isDone = checked)
    }

    // 삭제
    fun delete(id: Int) {
        _todos.removeAll { it.id == id }
    }

    // 수정
    fun update(id: Int, newTitle: String) {
        val index = _todos.indexOfFirst { it.id == id } // 목록을 돌면서 id가 같은 첫 번째 항목이 몇 번째 칸인지 찾아라
        if (index != -1) _todos[index] = _todos[index].copy(title = newTitle) // 찾았으면(-1이 아니면) 그 칸을 제목만 바뀐 새 객체로 교체
    }

    fun getTodo(id: Int): Todo? = _todos.find { it.id == id }

    // ★ 서버에서 할 일 가져오기
    fun loadFromServer() {
        viewModelScope.launch {          // 코루틴 시작 (화면을 멈추지 않음)
            isLoading = true
            errorMessage = null
            try {
                val serverTodos = repository.fetchSampleTodos()
                _todos.clear()
                _todos.addAll(serverTodos)
            } catch (e: Exception) {
                Log.e("TodoApp", "서버 호출 실패", e)          // 원인 전체를 로그로
                errorMessage = "불러오기 실패: ${e.message}"
            } finally {
                isLoading = false        // 성공이든 실패든 로딩 해제
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
