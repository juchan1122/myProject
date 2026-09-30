import AsyncStorage from "@react-native-async-storage/async-storage";
import {
    createContext,
    useContext,
    useEffect,
    useState,
    type ReactNode,
} from "react";

export type Todo = {
  id: string;
  title: string;
  done: boolean;
};

type TodoContextValue = {
  todos: Todo[];
  addTodo: (title: string) => void;
  toggleTodo: (id: string) => void;
  deleteTodo: (id: string) => void;
};

const TodoContext = createContext<TodoContextValue | null>(null);

const STORAGE_KEY = "todos";

export function TodoProvider({ children }: { children: ReactNode }) {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [isLoaded, setIsLoaded] = useState(false);

  // ① 앱이 시작될 때 한 번만 실행됨: 저장된 할 일 불러오기
  useEffect(() => {
    async function loadTodos() {
      try {
        const saved = await AsyncStorage.getItem(STORAGE_KEY);
        if (saved !== null) {
          setTodos(JSON.parse(saved));
        }
      } catch (error) {
        console.log("불러오기 실패:", error);
      } finally {
        setIsLoaded(true);
      }
    }
    loadTodos();
  }, []);

  // ② todos가 바뀔 때마다: 저장하기
  useEffect(() => {
    if (!isLoaded) {
      return;
    }
    async function saveTodos() {
      try {
        await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
        console.log("저장 완료:", todos.length, "개");
      } catch (error) {
        console.log("저장 실패:", error);
      }
    }
    saveTodos();
  }, [todos, isLoaded]);

  function addTodo(title: string) {
    const newTodo: Todo = { id: Date.now().toString(), title: title, done: false };
    setTodos([...todos, newTodo]);
  }

  function toggleTodo(id: string) {
    setTodos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, done: !todo.done } : todo
      )
    );
  }

  function deleteTodo(id: string) {
    setTodos(todos.filter((todo) => todo.id !== id));
  }

  return (
    <TodoContext value={{ todos, addTodo, toggleTodo, deleteTodo }}>
      {children}
    </TodoContext>
  );
}

export function useTodos() {
  const context = useContext(TodoContext);
  if (context === null) {
    throw new Error("useTodos는 TodoProvider 안에서만 사용할 수 있어요.");
  }
  return context;
}