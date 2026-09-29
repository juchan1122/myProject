


---

## 26.09.28 학습
- HTTP 통신: Retrofit으로 서버에서 할 일 목록 가져오기 (로딩·에러 처리 포함)
- 화면 3개 + 하단 탭: 홈 / 할 일 / 통계
- 디자인: 커스텀 색상 테마, 카드형 리스트, 다크모드 대응
- 동적 효과: 화면 전환 애니메이션, 진행률 링, 숫자 카운트업, 항목 추가/삭제 애니메이션

### 디렉터리 구조
```
com.juchan.todo
├─ MainActivity.kt
├─ data/
│  ├─ Todo.kt                    ← 도메인 모델 (그대로)
│  ├─ TodoRepository.kt          ← [새로] Service 계층
│  └─ remote/
│     ├─ TodoApi.kt              ← [새로] API 명세 + DTO
│     └─ NetworkModule.kt        ← [새로] Retrofit 설정
└─ ui/
   ├─ theme/Color.kt, Theme.kt   ← [수정] 디자인
   ├─ TodoNavHost.kt             ← [수정] 하단 탭 + 화면 전환
   ├─ home/HomeScreen.kt         ← [새로]
   ├─ stats/StatsScreen.kt       ← [새로]
   └─ todo/TodoScreen.kt, TodoEditScreen.kt, TodoViewModel.kt  ← [수정]
```
---
설정
```
# libs.versions.toml

    # [versions] 추가
    retrofit = "2.11.0"
    okhttpLogging = "4.12.0"
    kotlinxSerialization = "1.8.0"

    # [libraries] 추가
    retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
    retrofit-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
    okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttpLogging" }
    kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
    androidx-lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycleRuntimeKtx" }
    
    #[plugins] 추가
    kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }

----------------------------------------------------------------------------------
# build.gradle.kts (Project)

    plugins {
        alias(libs.plugins.android.application) apply false
        alias(libs.plugins.kotlin.compose) apply false
        alias(libs.plugins.kotlin.serialization) apply false   // 추가
    }
    
# build.gradle.kts (Module :app)

    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.compose)
        alias(libs.plugins.kotlin.serialization)               // 추가
    }
    
----------------------------------------------------------------------------------
# dependencies { }에 추가

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    
----------------------------------------------------------------------------------
   
```
Sync Now 클릭!!

---


인터넷 권한
```
# AndroidManifest.xml  <application> 태그 위에 한 줄 추가
    <uses-permission android:name="android.permission.INTERNET" />
    ★ 이게 없으면 네트워크 호출이 바로 실패
```

---

ㅏ