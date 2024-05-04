package sample.todolist.dto.auth.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AuthLoginRequest {

    @NotBlank(message = "사용자명은 필수입니다.")
    private String username;

    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;

    @Builder
    private AuthLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
