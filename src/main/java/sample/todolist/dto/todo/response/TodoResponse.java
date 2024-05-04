package sample.todolist.dto.todo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sample.todolist.domain.todo.TodoStatus;

import java.time.LocalDateTime;

@Getter
public class TodoResponse {

    private Long todoId;

    private String content;

    private TodoStatus todoStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDateTime;


    @QueryProjection
    @Builder
    public TodoResponse(Long todoId, String content, TodoStatus todoStatus, LocalDateTime createDateTime) {
        this.todoId = todoId;
        this.content = content;
        this.todoStatus = todoStatus;
        this.createDateTime = createDateTime;
    }
}
