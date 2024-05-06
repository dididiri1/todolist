package sample.todolist.controller.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.ApiResponse;
import sample.todolist.dto.EnumResponse;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;
import sample.todolist.dto.todo.response.TodoStatusUpdateResponse;
import sample.todolist.service.todo.TodoService;
import sample.todolist.util.EnumMapperType;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class TodoApiController {

    private final TodoService todoService;

    /**
     * @Method: getMemberDodoList
     * @Description: 투두 등록
     */
    @PostMapping("/api/v1/todos")
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoCreateRequest request) {
        todoService.createTodo(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "TODO 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * @Method: getTodoStatusList
     * @Description: 투두 카테고리
     */
    @GetMapping("/api/v1/todos/categories")
    public ResponseEntity<?> getTodoStatusList() {
        List<EnumResponse> response = todoService.getTodoStatus();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "TODO 상태 조회 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: updateTodoStatus
     * @Description: 투두 상태 수정
     */
    @PatchMapping("/api/v1/todos/{todoId}/status")
    public ResponseEntity<?> updateTodoStatus(@PathVariable Long todoId, @RequestBody @Valid TodoStatusUpdateRequest request) {
        TodoStatusUpdateResponse response = todoService.updateTodoStatus(todoId, request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "TODO 상태 수정 성공", response), HttpStatus.OK);
    }

}
