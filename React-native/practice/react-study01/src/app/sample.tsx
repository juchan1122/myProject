import { useEffect, useState } from "react";
import {
    ActivityIndicator,
    FlatList,
    Pressable,
    StyleSheet,
    Text,
    View,
} from "react-native";

import { useTodos } from "@/context/TodoContext";

type ApiTodo = {
  userId: number;
  id: number;
  title: string;
  completed: boolean;
};

const API_URL = "http://192.168.0.203:8080/api/todos";

export default function Sample() {
  const [items, setItems] = useState<ApiTodo[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const { addTodo } = useTodos();

  useEffect(() => {
    async function fetchTodos() {
      try {
        const response = await fetch(API_URL);
        if (!response.ok) {
          throw new Error(`서버 응답 오류: ${response.status}`);
        }
        const data: ApiTodo[] = await response.json();
        setItems(data);
      } catch (error) {
        setErrorMessage(String(error));
      } finally {
        setIsLoading(false);
      }
    }
    fetchTodos();
  }, []);

  if (isLoading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
        <Text>불러오는 중...</Text>
      </View>
    );
  }

  if (errorMessage !== null) {
    return (
      <View style={styles.center}>
        <Text style={styles.error}>{errorMessage}</Text>
      </View>
    );
  }

  return (
    <FlatList
      style={styles.list}
      data={items}
      keyExtractor={(item) => String(item.id)}
      renderItem={({ item }) => (
        <Pressable style={styles.item} onPress={() => addTodo(item.title)}>
          <Text>
            {item.completed ? "☑" : "☐"} {item.title}
          </Text>
          <Text style={styles.hint}>눌러서 내 목록에 추가</Text>
        </Pressable>
      )}
    />
  );
}

const styles = StyleSheet.create({
  center: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    gap: 12,
    padding: 20,
  },
  error: {
    color: "tomato",
    textAlign: "center",
  },
  list: {
    padding: 20,
  },
  item: {
    padding: 12,
    marginBottom: 8,
    backgroundColor: "white",
    borderRadius: 8,
    gap: 4,
  },
  hint: {
    color: "gray",
    fontSize: 12,
  },
});