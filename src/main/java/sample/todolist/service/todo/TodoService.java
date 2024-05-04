package sample.todolist.service.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.domain.todo.Todo;
import sample.todolist.domain.todo.TodoRepositoryJpa;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.response.TodoCreateResponse;
import sample.todolist.handler.ex.validationException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoService {

    private final TodoRepositoryJpa todoRepositoryJpa;

    private final MemberRepositoryJpa memberRepositoryJpa;

    @Transactional
    public TodoCreateResponse createTodo(TodoCreateRequest request) {
        Member member = validateMemberId(request.getMemberId());
        Todo todo = request.toEntity(member);
        Todo todoEntity = todoRepositoryJpa.save(todo);

        return TodoCreateResponse.of(todoEntity);

    }

    private Member validateMemberId(Long memberId) {
        Member findMember = memberRepositoryJpa.findById(memberId).orElseThrow(() -> {
            throw new validationException("해당 유저를 찾을수 없습니다.");
        });

        return findMember;
    }
}
