package sample.todolist.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.todolist.IntegrationTestSupport;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.handler.ex.validationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sample.todolist.dto.Role.ROLE_USER;

public class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
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
                .isInstanceOf(validationException.class)
                .hasMessage("이미 사용 중인 유저명입니다.");
    }

    private Member createMember(String username, String nickname) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname(nickname)
                .role(ROLE_USER)
                .build();
    }
}
