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
import sample.todolist.domain.todo.TodoRepositoryJpa;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.response.TodoCreateResponse;
import sample.todolist.service.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
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

    private Member createMember(String username, String nickname) {
        return Member.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode("1234"))
                .nickname(nickname)
                .role(ROLE_USER)
                .build();
    }
}
