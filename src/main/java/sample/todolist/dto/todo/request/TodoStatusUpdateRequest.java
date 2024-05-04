package sample.todolist.dto.todo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.todolist.domain.todo.TodoStatus;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TodoStatusUpdateRequest {

    @NotNull(message = "TODO 상태는 필수입니다.")
    private TodoStatus todoStatus;

    @Builder
    public TodoStatusUpdateRequest(TodoStatus todoStatus) {
        this.todoStatus = todoStatus;
    }
}
