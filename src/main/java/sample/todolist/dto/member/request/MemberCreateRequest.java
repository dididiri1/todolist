package sample.todolist.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.todolist.domain.member.Member;
import sample.todolist.dto.Role;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank(message = "유저명은 필수입니다.")
    private String username;

    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;


    @Builder
    private MemberCreateRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public Member toEntity(String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();

    }
}
