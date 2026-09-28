package com.juchan.todo.ui.theme.todo

import android.R.attr.checked
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juchan.todo.data.Todo
import com.juchan.todo.ui.theme.TodoAppTheme

// 임시로 쓸 더미 데이터
private val dummyTodos = listOf(
    Todo(1, "게임하기"),
    Todo(2, "Kotlin 공부하기", isDone = true),
    Todo(3, "개발 일지 쓰기"),
    Todo(4, "GitHub 커밋하기"),
    Todo(5, "장보기"),
    Todo(6, "Kotlin 공부하기"),
    Todo(7, "개발 일지 쓰기", isDone = true),
    Todo(8, "GitHub 커밋하기", isDone = true),
    Todo(9, "장보기", isDone = true),
    Todo(10, "Kotlin 공부하기"),
    Todo(11, "개발 일지 쓰기"),
    Todo(12, "GitHub 커밋하기"),
    Todo(13, "장보기"),
    Todo(14, "Kotlin 공부하기", isDone = true),
    Todo(15, "개발 일지 쓰기"),
    Todo(16, "GitHub 커밋하기"),
    Todo(17, "장보기"),
    Todo(18, "Kotlin 공부하기", isDone = true),
    Todo(19, "개발 일지 쓰기"),
    Todo(20, "GitHub 커밋하기"),
    Todo(21, "장보기"),
    Todo(22, "Kotlin 공부하기", isDone = true),
    Todo(23, "개발 일지 쓰기"),
    Todo(24, "GitHub 커밋하기"),
    Todo(25, "장보기"),
    Todo(26, "Kotlin 공부하기"),
    Todo(27, "개발 일지 쓰기"),
    Todo(28, "GitHub 커밋하기"),
)

// 화면 전체 (상단바 + 목록)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(){

    // 화면의 상태: 이 목록이 바뀌면 화면이 자동으로 그려짐
    val todos = remember { dummyTodos.toMutableStateList() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("할 일 목록 앱") })
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            // 입력창: 추가 버튼을 누르면 onAdd가 호출됨
            TodoInput(
                onAdd = { title ->
                    val newId = (todos.maxOfOrNull{ it.id} ?: 0) + 1
                    todos.add(Todo(id = newId, title = title))

                }
            )
            TodoList(
                todos = todos,

                // 체크박스를 누르면: 해당 항목을 완료 상태가 바뀐 복사본으로 교체
                onToggle = { todo, checked ->
                    val index = todos.indexOfFirst { it.id == todo.id }
                    if(index != -1) todos[index] = todo.copy(isDone = checked)
                },
                // 삭제 버튼을 누르면 목록에서 제거
                onDelete = { todo -> todos.remove(todo) }
            )
        }
    }
}


// 입력창 + 추가 버튼
@Composable
fun TodoInput(onAdd: (String) -> Unit, modifier: Modifier = Modifier){
    var text by remember { mutableStateOf("") } // 입력중인 글자

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,                       // 상태에 있는 글자를 보여주고
            onValueChange = {text = it },       // 입력하면 상태를 바꿈
            label = {Text("할 일 입력")},
            singleLine = true,
            modifier = Modifier.weight(1f)      // 남는 가로 공간을 다 차지
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onAdd(text.trim())
                text = ""                       // 추가 후 입력창 비우기
            },
            enabled = text.isNotBlank()         // 빈 글자면 버튼 비활성화
        ) {
            Text("추가")
        }
    }
}

// 스크롤되는 목록
@Composable
fun TodoList(
    todos: List<Todo>,
    onToggle: (Todo, Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(todos, key = { it.id }) { todo ->
            TodoItem(
                todo = todo,
                onToggle = { checked -> onToggle(todo, checked) },
                onDelete = { onDelete(todo) }
            )
            HorizontalDivider()

        }
    }
}

// 할 일 한 줄
@Composable
fun TodoItem(
    todo: Todo,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = onToggle        // [변경] null → 실제 동작 연결
        )
        Text(
            text = todo.title,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f)    // 글자가 남는 공간을 차지 → 삭제 버튼이 오른쪽 끝으로
        )
        TextButton(onClick = onDelete) {      // [새로 추가] 삭제 버튼
            Text("삭제")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    TodoAppTheme {
        TodoScreen()
    }
}