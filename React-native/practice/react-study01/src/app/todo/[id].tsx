import { router, useLocalSearchParams } from "expo-router";
import { Pressable, StyleSheet, Text, View } from "react-native";

import { useTodos } from "@/context/TodoContext";

export default function TodoDetail() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { todos, toggleTodo, deleteTodo } = useTodos();

  const todo = todos.find((item) => item.id === id);

  if (todo === undefined) {
    return (
      <View style={styles.container}>
        <Text>할 일을 찾을 수 없어요.</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.label}>제목</Text>
      <Text style={styles.value}>{todo.title}</Text>

      <Text style={styles.label}>상태</Text>
      <Text style={styles.value}>{todo.done ? "✅ 완료" : "⏳ 진행 중"}</Text>

      <Pressable style={styles.button} onPress={() => toggleTodo(todo.id)}>
        <Text style={styles.buttonText}>
          {todo.done ? "진행 중으로 되돌리기" : "완료 처리"}
        </Text>
      </Pressable>

      <Pressable
        style={[styles.button, styles.deleteButton]}
        onPress={() => {
          deleteTodo(todo.id);
          router.back();
        }}
      >
        <Text style={styles.buttonText}>삭제</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  label: {
    color: "gray",
    fontSize: 13,
    marginTop: 16,
  },
  value: {
    fontSize: 18,
  },
  button: {
    marginTop: 24,
    padding: 14,
    borderRadius: 8,
    backgroundColor: "skyblue",
    alignItems: "center",
  },
  deleteButton: {
    marginTop: 12,
    backgroundColor: "tomato",
  },
  buttonText: {
    color: "white",
    fontWeight: "bold",
  },
});