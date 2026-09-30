import { TodoProvider } from "@/context/TodoContext";
import { Stack } from "expo-router";

export default function RootLayout() {
  return (
    // <Link>와 router.push 
    //  글자를 누르면 바로 이동하는 단순한 경우엔 <Link>가 간편
    //  이벤트 처리 중에 이동하거나 조건에 따라 이동해야 할 땐 router.push를 사용
    <TodoProvider>
      <Stack>
        <Stack.Screen name="index" options={{ title: "나의 할 일" }} />
        <Stack.Screen name="about" options={{ title: "앱 정보" }} />
        <Stack.Screen name="todo/[id]" options={{ title: "할 일 상세" }} />
        <Stack.Screen name="sample" options={{ title: "서버에서 가져오기" }} />
      </Stack>
    </TodoProvider>
   );
}
