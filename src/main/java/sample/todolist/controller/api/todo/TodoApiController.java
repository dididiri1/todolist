package sample.todolist.controller.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.todolist.dto.ApiResponse;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;
import sample.todolist.dto.todo.response.TodoStatusUpdateResponse;
import sample.todolist.service.todo.TodoService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class TodoApiController {

    private final TodoService todoService;

    @PostMapping("/api/v1/todos")
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoCreateRequest request) {
        todoService.createTodo(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "TODO 등록 성공", null), HttpStatus.CREATED);
    }

    @PatchMapping("/api/v1/{todoId}/status")
    public ResponseEntity<?> updateTodoStatus(@PathVariable Long todoId, @RequestBody @Valid TodoStatusUpdateRequest request) {
        TodoStatusUpdateResponse response = todoService.updateTodoStatus(todoId, request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "TODO 상태 수정 성공", response), HttpStatus.OK);
    }

}
