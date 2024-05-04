package sample.todolist.controller.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.todolist.dto.ApiResponse;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.response.TodoCreateResponse;
import sample.todolist.service.todo.TodoService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class TodoApiController {

    private final TodoService todoService;

    @PostMapping("/api/v1/todos")
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoCreateRequest request) {
        todoService.createTodo(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "투두 등록 성공", null), HttpStatus.CREATED);
    }
}
