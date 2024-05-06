package sample.todolist.domain.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.todolist.IntegrationTestSupport;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.dto.todo.response.TodoResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.todolist.dto.Role.ROLE_USER;

@Transactional
public class TodoRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private EntityManager em;

    @Autowired
    private TodoQueryRepository todoQueryRepository;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private TodoRepositoryJpa todoRepositoryJpa;

    @DisplayName("TODO 리스트를 조회 한다.")
    @Test
    public void searchPageTodos() throws Exception {
        //given
        Member member = createMember("member1", "nickname1");
        memberRepositoryJpa.save(member);

        Todo todo1 = createTodo("할일 내용입니다 1", TodoStatus.TODO, member);
        Todo todo2 = createTodo("할일 내용입니다 2", TodoStatus.TODO, member);
        Todo todo3 = createTodo("할일 내용입니다 3", TodoStatus.TODO, member);
        todoRepositoryJpa.saveAll(List.of(todo1, todo2, todo3));

        PageRequest pageRequest = PageRequest.of(0, 2);

        em.flush();
        em.clear();

        //when
        Page<TodoResponse> todos = todoQueryRepository.searchPageTodos(member.getId(), pageRequest);

        //then
        assertThat(todos).hasSize(2)
                .extracting("content", "todoStatus")
                .containsExactlyInAnyOrder(
                        tuple("할일 내용입니다 3", TodoStatus.TODO),
                        tuple("할일 내용입니다 2", TodoStatus.TODO)
                );
    }

    @DisplayName("TODO 상태를 진행 중으로 변경 한다")
    @Test
    void updateTodoStatusToInProgress() throws Exception {
        //given
        Member member = createMember("member1", "nickname1");
        memberRepositoryJpa.save(member);

        Todo todo = createTodo("할일 내용입니다.", TodoStatus.TODO, member);
        todoRepositoryJpa.save(todo);

        TodoStatus newStatus = TodoStatus.IN_PROGRESS;
        todo.setTodoStatus(newStatus);

        em.flush();
        em.clear();

        //when
        Todo findTodo = todoRepositoryJpa.findById(todo.getId()).get();

        //then
        assertThat(findTodo.getTodoStatus()).isEqualTo(TodoStatus.IN_PROGRESS);

    }

    @DisplayName("TODO 상태를 완료로 변경 한다")
    @Test
    void updateTodoStatusToNone() throws Exception {
        //given
        Member member = createMember("member1", "nickname1");
        memberRepositoryJpa.save(member);

        Todo todo = createTodo("할일 내용입니다.", TodoStatus.TODO, member);
        todoRepositoryJpa.save(todo);

        TodoStatus newStatus = TodoStatus.DONE;
        todo.setTodoStatus(newStatus);

        em.flush();
        em.clear();
        //when
        Todo findTodo = todoRepositoryJpa.findById(todo.getId()).get();

        //then
        assertThat(findTodo.getTodoStatus()).isEqualTo(TodoStatus.DONE);

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
