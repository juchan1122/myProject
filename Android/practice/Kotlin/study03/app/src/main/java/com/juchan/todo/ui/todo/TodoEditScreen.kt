package com.juchan.todo.ui.todo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditScreen(
    todoId: Int,                  // 어떤 할 일을 수정할지
    viewModel: TodoViewModel,
    onBack: () -> Unit            // 뒤로 가기 요청을 위로 알림
) {
    val todo = viewModel.getTodo(todoId)

    // 입력 중인 제목. rememberSaveable: 화면을 회전해도 입력값 유지
    var title by rememberSaveable { mutableStateOf(todo?.title ?: "") }

    Scaffold(
        // 상단 바
        topBar = {
            TopAppBar(
                title = { Text("할 일 수정") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("뒤로") }
                }
            )
        }
    ) { innerPadding ->
        if (todo == null) {
            // 잘못된 id로 들어온 경우
            Text(
                text = "할 일을 찾을 수 없습니다.",
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("할 일") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(  // 저장 버튼을 눌러야 비로소 ViewModel에 반영
                    onClick = {
                        viewModel.update(todoId, title.trim())
                        onBack()                     // 저장 후 목록으로 돌아가기 onBack()으로 "돌아가 달라"고 알려
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("저장")
                }
            }
        }
    }
}