package com.juchan.todo.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


// object = 앱 전체에서 하나만 존재 (싱글턴)
object  NetworkModule {

    private  const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val json = Json{
        ignoreUnknownKeys = true  // 내가 정의 안 한 빌드가 와도 무시
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply{
                level = HttpLoggingInterceptor.Level.BODY  // 요청, 응답을 Logcat에 출력
            }
        )
        .build()

    val todoApi: TodoApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(TodoApi::class.java)  // 인터페이스의 실제 구현체를 만들어 줌
}