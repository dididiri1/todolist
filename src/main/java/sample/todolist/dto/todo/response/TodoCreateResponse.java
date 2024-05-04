package sample.todolist.dto.todo.response;

import lombok.Builder;
import lombok.Getter;
import sample.todolist.domain.todo.Todo;

import java.time.LocalDateTime;

@Getter
public class TodoCreateResponse {

    private Long todoId;

    private String content;

    @Builder
    private TodoCreateResponse(Long todoId, String content) {
        this.todoId = todoId;
        this.content = content;
    }

    public static TodoCreateResponse of(Todo todo) {
        return TodoCreateResponse.builder()
                .todoId(todo.getId())
                .content(todo.getContent())
                .build();

    }
}
