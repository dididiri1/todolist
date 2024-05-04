package sample.todolist.dto.todo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.todolist.domain.todo.Todo;
import sample.todolist.domain.todo.TodoStatus;

@Getter
@NoArgsConstructor
public class TodoStatusUpdateResponse {

    private Long todoId;

    private TodoStatus todoStatus;

    @Builder
    private TodoStatusUpdateResponse(Long todoId, TodoStatus todoStatus) {
        this.todoId = todoId;
        this.todoStatus = todoStatus;
    }

    public static TodoStatusUpdateResponse of(Todo todo) {
        return TodoStatusUpdateResponse.builder()
                .todoId(todo.getId())
                .todoStatus(todo.getTodoStatus())
                .build();
    }
}
