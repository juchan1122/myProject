package com.juchan.todo.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

// 서버가 주는 JSON 모양 그대로 (DTO)
@Serializable
data class TodoDto(
    val id: Int,
    val title: String,
    val completed: Boolean
)

// 인터페이스라 구현코드가 없음 -> 스프링의 Feign Client와 같은 방식
interface TodoApi {

    @GET("todos")
    suspend fun getTodos(@Query("_limit") limit: Int = 10): List<TodoDto>
}