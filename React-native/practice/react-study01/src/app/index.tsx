import { useTodos } from "@/context/TodoContext";
import { Link, router } from "expo-router";
import { useState } from "react";
import {
  FlatList,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";


// TodoItemProps : TodoItem 컴포넌트에 전달할 props의 타입 정의
type TodoItemProps = {
  title: string;
  done: boolean;
  onToggle: () => void;
  onDelete: () => void;
  onOpen: () => void;
};

// TodoInputProps : TodoInput 컴포넌트에 전달할 props의 타입 정의
type TodoInputProps = {
  onAdd: (title: string) => void;
}

/* ======================================================================================================================= */
// 할 일 목록 컴포넌트
function TodoItem( {title, done, onToggle, onDelete, onOpen} : TodoItemProps) {
  // {/* onPress : 할 일 완료 상태 토글, onLongPress : 길게 누르기 이벤트 */}
  return (
    <View style={styles.item}>
      <Pressable style={styles.itemTextArea} onPress={onToggle} onLongPress={onOpen}>  
        <Text style={done ? styles.doneText : undefined}>
          {done ? "☑" : "☐"} {title}
        </Text>
      </Pressable>
      <Pressable style={styles.deleteButton} onPress={onDelete}>
        <Text style={styles.deleteButtonText}>삭제</Text>
      </Pressable>
    </View>
  );
}

/* ======================================================================================================================= */

// 할 일 입력창 컴포넌트
function TodoInput({ onAdd }: TodoInputProps){
  const [text, setText] = useState("");

  function handleAdd(){
    if(text.trim() === ""){
      return;
    }
    onAdd(text.trim());
    console.log("할 일 추가:", text);
    setText("");
  }

  return (
      <View style={styles.inputRow}>
        <TextInput
          style={styles.input}
          value={text}    
          onChangeText={setText}
          onSubmitEditing={handleAdd}
          placeholder="할 일을 입력하세요"
        />
        <Pressable style={styles.addButton} onPress={handleAdd}>
          <Text style={styles.addButtonText}>추가</Text>
        </Pressable>
      </View>
  );
}

/* ======================================================================================================================= */
/* ======================================================================================================================= */
// Index 컴포넌트
export default function Index() {
  const { todos, addTodo, toggleTodo, deleteTodo } = useTodos();
  const doneCount = todos.filter((todo) => todo.done).length;

  return (
    <View style={styles.container}>
      <Text style={styles.title}>
        나의 할 일 (완료 {doneCount}/{todos.length})
      </Text>
      <Link href="/about" style={styles.link}>
        앱 정보 보기 ›
      </Link>
      <Link href="/sample" style={styles.link}>
        서버에서 할 일 가져오기 ›
      </Link>
      <TodoInput onAdd={addTodo} />
      <FlatList
        data={todos}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <TodoItem
            title={item.title}
            done={item.done}
            onToggle={() => toggleTodo(item.id)}
            onDelete={() => deleteTodo(item.id)}
            onOpen={() =>
              router.push({ pathname: "/todo/[id]", params: { id: item.id } })
            }
          />
        )}
        ListEmptyComponent={<Text style={styles.empty}>할 일이 없어요 🎉</Text>}
      />
    </View>
  );
}
/* ======================================================================================================================= */
/* ======================================================================================================================= */















const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "lightyellow",
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 12,
  },
  item: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 8,
    backgroundColor: "white",
    borderRadius: 8,
  },
  itemTextArea: {
    flex: 1,
    padding: 12,
  },
  doneText: {
    color: "gray",
    textDecorationLine: "line-through",
  },
  deleteButton: {
    paddingHorizontal: 14,
    paddingVertical: 12,
  },
  deleteButtonText: {
    color: "tomato",
    fontWeight: "bold",
  },
  inputRow: {
    flexDirection: "row",
    gap: 8,
    marginBottom: 12,
  },
  input: {
    flex: 1,
    backgroundColor: "white",
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
  },
  addButton: {
    backgroundColor: "skyblue",
    borderRadius: 8,
    paddingHorizontal: 16,
    justifyContent: "center",
  },
  addButtonText: {
    fontWeight: "bold",
  },
  empty: {
    color: "gray",
    textAlign: "center",
    marginTop: 40,
  },
  link: {
    color: "royalblue",
    marginBottom: 12,
  },
});