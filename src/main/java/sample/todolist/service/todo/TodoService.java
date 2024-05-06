package sample.todolist.service.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.domain.todo.Todo;
import sample.todolist.domain.todo.TodoRepositoryJpa;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.EnumResponse;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;
import sample.todolist.dto.todo.response.TodoCreateResponse;
import sample.todolist.dto.todo.response.TodoStatusUpdateResponse;
import sample.todolist.handler.ex.ValidationException;
import sample.todolist.util.EnumMapperType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new ValidationException("해당 유저를 찾을수 없습니다.");
        });

        return findMember;
    }

    @Transactional
    public TodoStatusUpdateResponse updateTodoStatus(Long todoId, TodoStatusUpdateRequest request) {
        Todo findTodo = validateTodoId(todoId);
        checkIfPendingAndInProgress(request, findTodo);

        findTodo.setTodoStatus(request.getTodoStatus());
        Todo saveTodo = todoRepositoryJpa.save(findTodo);

        return TodoStatusUpdateResponse.of(saveTodo);
    }

    private void checkIfPendingAndInProgress(TodoStatusUpdateRequest request, Todo findTodo) {
        if (request.getTodoStatus() == TodoStatus.PENDING && findTodo.getTodoStatus() != TodoStatus.IN_PROGRESS) {
            throw new ValidationException("진행 중 상태에서만 대기 상태로 변경될 수 있습니다.");
        }
    }

    private Todo validateTodoId(Long todoId) {
        Todo findTodo = todoRepositoryJpa.findById(todoId).orElseThrow(() -> {
            throw new ValidationException("해당 게시글을 찾을수 없습니다.");
        });

        return findTodo;
    }

    public List<EnumResponse> getTodoStatus() {
        Class<? extends EnumMapperType> e = TodoStatus.class;
        List<EnumResponse> response = Arrays.stream(e.getEnumConstants()).map(EnumResponse::new).collect(Collectors.toList());
        return response;
    }
}
