package sample.todolist.dto.todo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.todo.Todo;
import sample.todolist.domain.todo.TodoStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TodoCreateRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotBlank(message = "할일은 필수입니다.")
    private String content;


    @Builder
    private TodoCreateRequest(Long memberId, String content) {
        this.memberId = memberId;
        this.content = content;
    }

    public Todo toEntity(Member member) {
        return Todo.builder()
                .content(content)
                .todoStatus(TodoStatus.TODO)
                .member(member)
                .build();

    }
}
