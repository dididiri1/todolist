package sample.todolist.service.todo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.todolist.IntegrationTestSupport;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.domain.todo.Todo;
import sample.todolist.domain.todo.TodoRepositoryJpa;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;
import sample.todolist.dto.todo.response.TodoCreateResponse;
import sample.todolist.dto.todo.response.TodoStatusUpdateResponse;
import sample.todolist.handler.ex.validationException;
import sample.todolist.service.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sample.todolist.dto.Role.ROLE_USER;

public class TodoServiceTest extends IntegrationTestSupport  {

    @Autowired
    private TodoService todoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private TodoRepositoryJpa todoRepositoryJpa;

    @AfterEach
    void tearDown() {
        todoRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("TODO 할일을 등록 한다.")
    @Test
    void createTodo() throws Exception {
        //given
        Member member = createMember("testUser", "홍길동");
        memberRepositoryJpa.save(member);

        TodoCreateRequest request = TodoCreateRequest.builder()
                .memberId(member.getId())
                .content("할일 내용입니다.")
                .build();

        //when
        TodoCreateResponse response = todoService.createTodo(request);

        //then
        Assertions.assertThat(response.getTodoId()).isNotNull();
        assertThat(response)
                .extracting("todoId", "content")
                .contains(response.getTodoId(), "할일 내용입니다.");
    }

    @DisplayName("TODO 상태를 변경 한다.")
    @Test
    void updateTodoStatus() throws Exception {
        //given
        Member member = createMember("member1", "nickname1");
        memberRepositoryJpa.save(member);

        Todo todo = createTodo("할일 내용입니다.", TodoStatus.TODO, member);
        todoRepositoryJpa.save(todo);


        TodoStatusUpdateRequest request = TodoStatusUpdateRequest.builder()
                .todoStatus(TodoStatus.IN_PROGRESS)
                .build();

        //when
        TodoStatusUpdateResponse response = todoService.updateTodoStatus(todo.getId(), request);

        //then
        Assertions.assertThat(response.getTodoId()).isNotNull();
        assertThat(response)
                .extracting("todoId", "todoStatus")
                .contains(response.getTodoId(), TodoStatus.IN_PROGRESS);
    }

    @DisplayName("TODO 대기 상태로 변경 시 진행 중 상태가 아니면 예외가 발생한다.")
    @Test
    void updateTodoStatusToPending() throws Exception {
        //given
        Member member = createMember("member1", "nickname1");
        memberRepositoryJpa.save(member);

        Todo todo = createTodo("할일 내용입니다.", TodoStatus.TODO, member);
        todoRepositoryJpa.save(todo);

        TodoStatusUpdateRequest request = TodoStatusUpdateRequest.builder()
                .todoStatus(TodoStatus.PENDING )
                .build();

        //when then
        assertThatThrownBy(() -> todoService.updateTodoStatus(todo.getId(), request))
                .isInstanceOf(validationException.class)
                .hasMessage("진행 중 상태에서만 대기 상태로 변경될 수 있습니다.");
    }

    private Todo createTodo(String content, TodoStatus todoStatus, Member member) {
        return Todo.builder()
                .content(content)
                .todoStatus(todoStatus)
                .member(member)
                .build();
    }

    private Member createMember(String username, String nickname) {
        return Member.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode("1234"))
                .nickname(nickname)
                .role(ROLE_USER)
                .build();
    }
}
