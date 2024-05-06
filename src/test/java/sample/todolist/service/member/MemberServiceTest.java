package sample.todolist.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.todolist.IntegrationTestSupport;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.domain.todo.Todo;
import sample.todolist.domain.todo.TodoRepositoryJpa;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.dto.todo.response.TodoResponse;
import sample.todolist.handler.ex.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;
import static sample.todolist.dto.Role.ROLE_USER;

public class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private TodoRepositoryJpa todoRepositoryJpa;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        todoRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("신규 회원을 등록 한다.")
    @Test
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("test")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("홍길동")
                .build();

        //when
        MemberCreateResponse response = memberService.createMember(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("test");
        assertThat(response.getNickname()).isEqualTo("홍길동");
    }

    @DisplayName("중복된 사용자 이름으로 회원가입할 경우 예외가 발생한다.")
    @Test
    void createUserWithDuplicateUsername() throws Exception {
        //given
        Member member = createMember("testUser", "홍길동");
        memberRepositoryJpa.save(member);

        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("testUser")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .nickname("닉네임")
                .build();

        //when //then
        assertThatThrownBy(() -> memberService.createMember(request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("이미 사용 중인 유저명입니다.");
    }

    @DisplayName("회원을 탈퇴 한다.")
    @Test
    void deleteMember() throws Exception {
        //given
        Member member = createMember("testUser", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        memberService.deleteMember(member.getId());

        //then
        Optional<Member> findMember = memberRepositoryJpa.findById(member.getId());
        assertThat(findMember.isPresent()).isFalse();
    }

    @DisplayName("TODO 리스트 1페이지를 조회한다.")
    @Test
    void getTodoListWithPage1() throws Exception {
        //given
        Member member = createMember("member1", "nickname1");
        memberRepositoryJpa.save(member);

        List<Todo> requestPosts = IntStream.range(1, 21)
                .mapToObj(i -> Todo.builder()
                        .content("할일 내용 " + i)
                        .todoStatus(TodoStatus.TODO)
                        .member(member)
                        .build())
                .collect(Collectors.toList());

        todoRepositoryJpa.saveAll(requestPosts);

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<TodoResponse> result = memberService.getMemberDodoList(member.getId(), pageRequest);

        //then
        assertThat(result).hasSize(3)
                .extracting("content", "todoStatus")
                .containsExactlyInAnyOrder(
                        tuple("할일 내용 20", TodoStatus.TODO),
                        tuple("할일 내용 19", TodoStatus.TODO),
                        tuple("할일 내용 18", TodoStatus.TODO)
                );
        assertThat(result.isFirst()).isTrue();
        assertThat(result.hasNext()).isTrue();
    }

    private Member createMember(String username, String nickname) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname(nickname)
                .role(ROLE_USER)
                .build();
    }

    private Todo createTodo(String content, TodoStatus todoStatus, Member member) {
        return Todo.builder()
                .content(content)
                .todoStatus(todoStatus)
                .member(member)
                .build();
    }
}
