package com.example.todoapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final Map<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public TodoController(){
        save("우유 사기");
        save("Spring Boot 연결 하기");
    }


    @GetMapping
    public List<Todo> findAll(){
        return store.values().stream()
                .sorted(Comparator.comparing(Todo::id))
                .toList();
    }

    @PatchMapping("/{id}/toggle")
    public Todo toggle(@PathVariable Long id){
        Todo todo = store.get(id);
        if (todo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "할 일을 찾을 수 없습니다: " + id);
        }
        Todo toggled = new Todo(todo.id(), todo.title(), !todo.done());
        store.put(id, toggled);
        return toggled;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        store.remove(id);
    }

    private Todo save(String title){
        Long id = sequence.incrementAndGet();
        Todo todo = new Todo(id, title, false);
        store.put(id, todo);
        return todo;
    }
}
