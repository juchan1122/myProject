package com.juchan.todo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juchan.todo.ui.home.HomeScreen
import com.juchan.todo.ui.stats.StatsScreen
import com.juchan.todo.ui.todo.TodoViewModel
import com.juchan.todo.ui.todo.TodoEditScreen
import com.juchan.todo.ui.todo.TodoScreen

private data class Tab(val route: String, val label: String, val icon: String)

private val tabs = listOf(
    Tab("home", "홈", "🏠"),
    Tab("list", "할 일", "✅"),
    Tab("stats", "통계", "📊"),
)



@Composable
fun TodoNavHost() {
    val navController = rememberNavController()
    val todoViewModel: TodoViewModel = viewModel()

    // 현재 어느 화면인지 추적
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = tabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            // 수정 화면에서는 하단 탭이 아래로 사라짐
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    // 탭을 눌러도 백스택이 쌓이지 않게
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(tab.icon, fontSize = 20.sp) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 8 } },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 8 } }
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = todoViewModel,
                    onGoToList = { navController.navigate("list") }
                )
            }
            composable("list") {
                TodoScreen(
                    viewModel = todoViewModel,
                    onTodoClick = { id -> navController.navigate("edit/$id") }
                )
            }
            composable("stats") {
                StatsScreen(viewModel = todoViewModel)
            }
            composable(
                route = "edit/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.IntType })
            ) { entry ->
                val todoId = entry.arguments?.getInt("todoId") ?: 0
                TodoEditScreen(
                    todoId = todoId,
                    viewModel = todoViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}


// Navigation -> 스프링 URL 매핑과 같은 구조
//@Composable
//fun TodoNavHost() {
//    val navController = rememberNavController()     // 화면 이동 리모컨
//    val todoViewModel: TodoViewModel = viewModel()   // 두 화면이 함께 쓰는 ViewModel (한 번만 생성)
//
//    // startDestination = "list "-> 앱을 켜면 list 주소부터 보여줘라
//    NavHost(navController = navController, startDestination = "list") {
//
//        // 주소 "list" → 목록 화면  | = 스프링 @GetMapping("/list") | list라는 주소로 오면 이 화면을 띄워라
//        composable("list") {
//            TodoScreen(
//                viewModel = todoViewModel,
//                onTodoClick = { id -> navController.navigate("edit/$id") }
//            )
//        }
//
//        // 주소 "edit/{todoId}" → 수정 화면  |  = 스프링 @GetMapping("/edit/{todoId}")
//        composable(
//            route = "edit/{todoId}",
//            arguments = listOf(navArgument("todoId") { type = NavType.IntType }) // 이 값은 숫자다"라고 타입을 알려주는 부분
//        ) { backStackEntry ->
//            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0   // 실제로 그 값을 꺼내는 코드
//            TodoEditScreen(
//                todoId = todoId,
//                viewModel = todoViewModel,
//                onBack = { navController.popBackStack() }  // | 스프링 - 브라우저 뒤로 가기 | 돌아가 달라는 요청이 오면 화면 하나를 걷어내라
//            )
//        }
//    }
//}