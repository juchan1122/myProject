import { useState } from "react";
import { Pressable, StyleSheet, Text, TextInput, View } from "react-native";


type Todo = {
  id: string;
  title: string;
}

type TodoItemProps = {
  title: string;
};




function TodoItem( {title} : TodoItemProps) {
  const [done, setDone] = useState(false); // 입력창의 글자를 기억하는 State

  return (
    <Pressable style={styles.item} onPress={() => setDone(!done)}> 
      <Text style={done ? styles.doneText : undefined}>
        {done ? "☑" : "☐"} {title}
      </Text>
    </Pressable>
  );
}


type TodoInputProps = {
  onAdd: (title: string) => void;
}


// ① "우" 입력
// ② onChangeText가 setText("우") 호출
// ③ React가 text를 "우"로 바꾸고 다시 그림
// ④ value={text} 이므로 입력창에 "우" 표시
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
    // value={text} : 입력창에 입력한 글자를 기억하는 State
    // onChangeText={setText} : 입력창에 글자를 입력할 때마다 setText 함수가 실행되어 text State를 업데이트
    // setText("우") -> setText("우유") -> setText("우유 사") -> setText("우유 사기")
    <View>
      <View style={styles.inputRow}>
        <TextInput
          style={styles.input}
          value={text}    
          onChangeText={setText}
          placeholder="할 일을 입력하세요"
        />
        <Pressable style={styles.addButton} onPress={handleAdd}>
          <Text style={styles.addButtonText}>추가</Text>
        </Pressable>
      </View>
      <Text style={styles.preview}>입력 중: {text}</Text>
    </View>
  );
}


export default function Index() {
  const [todos, setTodos] = useState<Todo[]>([
    { id: "1", title: "🍞 우유 사기" },
    { id: "2", title: "🍞 빵 사기" },
    { id: "3", title: "🥚 계란 사기" },
  ]);

  function addTodo(title: string) {
    const newTodo: Todo = {
      id: Date.now().toString(), 
      title: title
    };
    setTodos([...todos, newTodo]);
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>나의 할 일({todos.length})</Text>
      <TodoInput onAdd={addTodo} />
      {
        todos.map((todo) => (
          <TodoItem key={todo.id} title={todo.title} />
        ))
      }
    </View>
  );
}

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
    padding: 12,
    marginBottom: 8,
    backgroundColor: "white",
    borderRadius: 8,
  },
  doneText: {
    color: "gray",
    textDecorationLine: "line-through",
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
  preview: {
    color: "gray",
    marginBottom: 12,
  },
});
