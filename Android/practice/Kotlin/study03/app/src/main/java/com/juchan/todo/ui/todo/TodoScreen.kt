package com.juchan.todo.ui.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juchan.todo.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    onTodoClick: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // 에러가 생기면 스낵바로 알리고 상태를 비움
    LaunchedEffect(viewModel.errorMessage) {
        val message = viewModel.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("할 일", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(
                        onClick = { viewModel.loadFromServer() },
                        enabled = !viewModel.isLoading
                    ) {
                        Text(if (viewModel.isLoading) "불러오는 중..." else "서버에서 가져오기")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            // 로딩 바: 로딩 중일 때만 스르륵 나타남
            AnimatedVisibility(
                visible = viewModel.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            TodoInput(onAdd = { title -> viewModel.add(title) })

            if (viewModel.todos.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.todos, key = { it.id }) { todo ->
                        TodoItem(
                            todo = todo,
                            onToggle = { checked -> viewModel.toggle(todo.id, checked) },
                            onDelete = { viewModel.delete(todo.id) },
                            onClick = { onTodoClick(todo.id) },
                            modifier = Modifier.animateItem()   // 추가·삭제 시 자연스럽게 이동
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodoInput(onAdd: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("무엇을 하실 건가요?") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(10.dp))
        FilledIconButton(
            onClick = {
                onAdd(text.trim())
                text = ""
            },
            enabled = text.isNotBlank(),
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("+", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 완료 여부에 따라 글자색이 부드럽게 변함
    val titleColor by animateColorAsState(
        targetValue = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onSurface,
        label = "titleColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = todo.isDone, onCheckedChange = onToggle)
            Text(
                text = todo.title,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor,
                textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp)
            )
            TextButton(onClick = onDelete) {
                Text("삭제", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📝", fontSize = 48.sp)
        Spacer(Modifier.height(12.dp))
        Text("할 일이 없습니다", style = MaterialTheme.typography.titleMedium)
        Text(
            "위에서 추가하거나 서버에서 가져와 보세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}