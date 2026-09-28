package com.juchan.todo.data

import com.juchan.todo.data.remote.NetworkModule

class TodoRepository {

    suspend fun fetchSampleTodos(): List<Todo>{
        return NetworkModule.todoApi.getTodos(limit = 10)
            .map { dto ->                               // DTO → 우리 앱의 모델로 변환
                Todo(
                    id = dto.id,
                    title = dto.title,
                    isDone = dto.completed              // 서버는 completed, 우리는 isDone

                    // 서버 응답 모양(completed)과 우리 앱 모양(isDone)을 여기서 분리
                    // 나중에 서버 필드명이 바뀌어도 이 파일만 고치면 화면은 손댈 필요가 없습
                )

            }
    }
}