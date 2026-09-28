package com.juchan.todo.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juchan.todo.ui.todo.TodoViewModel

@Composable
fun HomeScreen(
    viewModel: TodoViewModel,
    onGoToList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "안녕하세요 👋",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "오늘의 할 일",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // 진행률 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProgressRing(progress = viewModel.progress)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = if (viewModel.remainCount == 0) "모두 끝냈어요! 🎉"
                    else "${viewModel.remainCount}개 남았어요",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 요약 카드 3개
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("전체", viewModel.total, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            SummaryCard("완료", viewModel.doneCount, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
            SummaryCard("남음", viewModel.remainCount, MaterialTheme.colorScheme.error, Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onGoToList,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("할 일 관리하러 가기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// 애니메이션 진행률 링 (Canvas로 직접 그림)
@Composable
fun ProgressRing(progress: Float, modifier: Modifier = Modifier) {
    val animated by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 900),
        label = "progress"
    )
    val trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
    val ringColor = MaterialTheme.colorScheme.primary

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(150.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 16.dp.toPx()
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)

            drawArc(                       // 배경 링
                color = trackColor,
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = topLeft, size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(                       // 진행 링
                color = ringColor,
                startAngle = -90f,         // 12시 방향에서 시작
                sweepAngle = 360f * animated,
                useCenter = false,
                topLeft = topLeft, size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${(animated * 100).toInt()}%",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

// 숫자가 올라가는 요약 카드
@Composable
fun SummaryCard(label: String, value: Int, accent: Color, modifier: Modifier = Modifier) {
    val animatedValue by animateIntAsState(
        targetValue = value,
        animationSpec = tween(600),
        label = "count"
    )
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text("$animatedValue", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold, color = accent)
        }
    }
}